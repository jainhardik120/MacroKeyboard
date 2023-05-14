package com.jainhardik120.macrokeyboard.ui.presentation.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainhardik120.macrokeyboard.data.local.entity.ScreenEntity
import com.jainhardik120.macrokeyboard.domain.repository.MacroRepository
import com.jainhardik120.macrokeyboard.util.Screen
import com.jainhardik120.macrokeyboard.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.PrintWriter
import java.net.Socket
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MacroRepository
) : ViewModel() {
    var state by mutableStateOf(HomeState())
    private val TAG = "HomeViewModel"

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var screenInfo = repository.getScreen(state.currentScreen.last())
    private lateinit var socket: Socket
    private lateinit var printWriter: PrintWriter

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    init {
        Log.d(TAG, "HomeViewModel: Initialized")
    }

    private fun openConnection() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try{
                    val portInfo = repository.getConnectionInfo()
                    socket = Socket(portInfo.first, portInfo.second)
                    printWriter = PrintWriter(socket.getOutputStream(), true)
                    state = state.copy(connectedState = true)
                } catch (e:Exception){
                    state = state.copy(connectedState = false)
                    sendUiEvent(UiEvent.ShowSnackbar(e.message?:"Unknown Error"))
                }

            }
        }
    }

    private fun closeConnection() {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                printWriter.close()
                socket.close()
                state = state.copy(connectedState = false)
            } catch (e:Exception){
                state = state.copy(connectedState = false)
                sendUiEvent(UiEvent.ShowSnackbar(e.message?:"Unknown Error"))
            }

        }
    }


    fun navigateNewButton() {
        sendUiEvent(UiEvent.Navigate(Screen.EditScreen.withArgs("0", "0")))
    }


    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnSettingsButtonClicked -> {
                sendUiEvent(UiEvent.Navigate(Screen.SettingsScreen.route))
            }

            is HomeScreenEvent.OnButtonClicked -> {
                handleButtonPress(event.screenEntity)
            }

            is HomeScreenEvent.OnButtonLongClicked -> {
                sendUiEvent(
                    UiEvent.Navigate(
                        "${Screen.EditScreen.route}/${
                            state.currentScreen.last().toString()
                        }?childId=${event.screenEntity.childId.toString()}"
                    )
                )
            }

            is HomeScreenEvent.BackPressed -> {
                if (state.currentScreen.size > 1) {
                    val list = List<Int>(state.currentScreen.size - 1) {
                        state.currentScreen[it]
                    }
                    state = state.copy(currentScreen = list)
                    screenInfo = repository.getScreen(state.currentScreen.last())
                }
            }

            is HomeScreenEvent.OnNewButtonClicked -> {
                sendUiEvent(
                    UiEvent.Navigate(
                        Screen.EditScreen.withArgs(
                            state.currentScreen.last().toString()
                        )
                    )
                )
            }

            is HomeScreenEvent.CloseClicked -> {
                if (!state.connectedState) {
                    openConnection()
                } else {
                    closeConnection()
                }
            }
        }
    }

    private fun handleButtonPress(screenEntity: ScreenEntity) {
        if (screenEntity.type == 1) {
            viewModelScope.launch {
                val list = screenEntity.childId?.let { repository.getButtonActions(it) }
                if (list != null) {
                    if (!state.connectedState) {
                        openConnection()
                    }
                    val array = JSONArray()
                    for (i in list) {
                        val jsonObject = JSONObject()
                        jsonObject.put("type", i.type)
                        jsonObject.put("data", i.data)
                        array.put(jsonObject)
                    }
                    withContext(Dispatchers.IO) {
                        try {
                            printWriter.print(array)
                            printWriter.flush()
                        } catch (e: Exception) {
                            sendUiEvent(UiEvent.ShowSnackbar(e.message ?: "Unknown Error"))
                        }
                    }
                }
            }
        } else {
            val list = List(state.currentScreen.size + 1) {
                if (it < state.currentScreen.size) {
                    state.currentScreen[it]
                } else {
                    screenEntity.childId!!
                }
            }
            state = state.copy(currentScreen = list)
            screenInfo = repository.getScreen(state.currentScreen.last())
        }
    }

    fun initialize() {
        openConnection()
    }

}
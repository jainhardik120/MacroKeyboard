package com.jainhardik120.macrokeyboard.ui.presentation.home

import android.media.midi.MidiDeviceInfo.PortInfo
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainhardik120.macrokeyboard.data.local.entity.ScreenEntity
import com.jainhardik120.macrokeyboard.domain.repository.MacroRepository
import com.jainhardik120.macrokeyboard.util.Resource
import com.jainhardik120.macrokeyboard.util.Screen
import com.jainhardik120.macrokeyboard.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.PrintWriter
import java.net.Socket
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MacroRepository
) : ViewModel(){
    var state by mutableStateOf(HomeState())
    private val TAG = "HomeViewModel"

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    val screenInfo = repository.getScreen(state.currentScreen)

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    init {
        Log.d(TAG, "HomeViewModel: Initialized")

    }


    fun navigateNewButton(){
        sendUiEvent(UiEvent.Navigate(Screen.EditScreen.withArgs("0","0")))
    }


    fun onEvent(event:HomeScreenEvent){
        when(event){
            is HomeScreenEvent.OnSettingsButtonClicked->{
                sendUiEvent(UiEvent.Navigate(Screen.SettingsScreen.route))
            }
            is HomeScreenEvent.OnButtonClicked->{
                handleButtonPress(event.screenEntity)
            }
            is HomeScreenEvent.OnButtonLongClicked->{
                sendUiEvent(UiEvent.Navigate("${Screen.EditScreen.route}/${state.currentScreen.toString()}?childId=${event.screenEntity.childId.toString()}"))
            }
            is HomeScreenEvent.BackPressed->{

            }
            HomeScreenEvent.OnNewButtonClicked -> {
                sendUiEvent(UiEvent.Navigate(Screen.EditScreen.withArgs(state.currentScreen.toString())))
            }
        }
    }

    private fun handleButtonPress(screenEntity: ScreenEntity){
        if(screenEntity.type==1){
            viewModelScope.launch {
                val list = screenEntity.childId?.let { repository.getButtonActions(it) }
                if (list != null) {
                    Log.d(TAG, "handleButtonPress: ${list.size}")
                    for (i in list){
                        val dataObject = "{\"type\":\"${i.type}\",\"data\":\"${i.data}\"}"
                        sendData(dataObject)
                    }
                }
            }
        } else {
            // TODO: Change Screen Implementation
        }

    }

    suspend fun sendData(message: String){
        withContext(Dispatchers.IO) {
            try{
                val portInfo = repository.getConnectionInfo()
                val client = Socket(portInfo.first, portInfo.second)
                val printWriter = PrintWriter(client.getOutputStream(), true)
                printWriter.write(message)
                printWriter.flush()
                printWriter.close()
                client.close()
            } catch (e:Exception){
//                Log.d(TAG, "sendData: ${e.message}")
                e.message?.let { UiEvent.ShowSnackbar(it) }?.let { sendUiEvent(it) }
            }
            
        }
    }
}
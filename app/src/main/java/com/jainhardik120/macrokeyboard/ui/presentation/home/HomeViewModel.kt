package com.jainhardik120.macrokeyboard.ui.presentation.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                handleButtonPress(id = event.id)
            }
            is HomeScreenEvent.OnButtonLongClicked->{
                handleLongButtonPress(id = event.id)
            }
            is HomeScreenEvent.BackPressed->{

            }
        }
    }

    private fun handleButtonPress(id : Int){
        if(id==state.screenEntities.size){
            sendUiEvent(UiEvent.Navigate(Screen.EditScreen.withArgs(state.currentScreen.toString(), id.toString())))
        } else {
            if(state.screenEntities[id].type==0){
                // Screen Type
            } else {
                // Button Type
            }
        }
    }

    private fun handleLongButtonPress(id : Int){
        if(id!=state.screenEntities.size){
            sendUiEvent(UiEvent.Navigate(Screen.EditScreen.withArgs(state.currentScreen.toString(), id.toString())))
        }
    }

    suspend fun sendData(ipAddress:String, port: Int, message: String){
        withContext(Dispatchers.IO) {
            val client = Socket(ipAddress, port)
            val printWriter = PrintWriter(client.getOutputStream(), true)
            printWriter.write(message)
            printWriter.flush()
            printWriter.close()
            client.close()
        }
    }
}
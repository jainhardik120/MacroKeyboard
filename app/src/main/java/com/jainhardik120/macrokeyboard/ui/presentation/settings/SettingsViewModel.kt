package com.jainhardik120.macrokeyboard.ui.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainhardik120.macrokeyboard.domain.repository.MacroRepository
import com.jainhardik120.macrokeyboard.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: MacroRepository
) : ViewModel() {
    var state by mutableStateOf(SettingsState())

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    init {
        val pair = repository.getConnectionInfo()
        state = state.copy(ipAddress = pair.first, port = pair.second.toString())
    }

    fun onEvent(event: SettingsScreenEvent){
        when(event){
            is SettingsScreenEvent.ipAddressChanged->{
                state = state.copy(ipAddress = event.ipAddress)
            }
            is SettingsScreenEvent.portChanged->{
                state = state.copy(port = event.port)
            }
            is SettingsScreenEvent.onSaveButtonClicked->{
                if(state.ipAddress.isNotEmpty() && state.port.isNotEmpty()){
                    repository.updatePortInfo(Pair(state.ipAddress, state.port.toInt()))
                    sendUiEvent(UiEvent.Navigate())
                }else{
                    if(state.ipAddress.isEmpty() && state.port.isEmpty()){
                        sendUiEvent(UiEvent.ShowSnackbar("Enter IP Address and Port"))
                    }else if(state.port.isEmpty()){
                        sendUiEvent(UiEvent.ShowSnackbar("Enter Port"))
                    }else {
                        sendUiEvent(UiEvent.ShowSnackbar("Enter IP Address"))
                    }
                }

            }
            else -> {}
        }
    }
}
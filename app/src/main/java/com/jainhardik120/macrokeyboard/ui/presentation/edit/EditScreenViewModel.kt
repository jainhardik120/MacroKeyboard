package com.jainhardik120.macrokeyboard.ui.presentation.edit

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainhardik120.macrokeyboard.domain.repository.MacroRepository
import com.jainhardik120.macrokeyboard.ui.presentation.settings.SettingsState
import com.jainhardik120.macrokeyboard.util.Screen
import com.jainhardik120.macrokeyboard.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MacroRepository
) : ViewModel() {

    private val TAG = "EditScreenViewModel"
    var state by mutableStateOf(EditButtonState())

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            val screenId = savedStateHandle.get<String>("screenId")?:return@launch
            val childId = savedStateHandle.get<String>("childId")?:return@launch
            state = state.copy(screenId = screenId, childId = childId)
            val initialData = repository.getButtonDetails(screenId = screenId.toInt(), childId = childId.toInt())
            if (initialData != null) {
                state = state.copy(label = initialData.label, type = initialData.type)
            }
            Log.d(TAG, "Initialize: ${state.toString()}")
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }


    fun onEvent(event: EditButtonScreenEvent){
        when(event){
            is EditButtonScreenEvent.SaveButtonClick->{
                sendUiEvent(UiEvent.Navigate())
            }
            else->{}
        }
    }
}
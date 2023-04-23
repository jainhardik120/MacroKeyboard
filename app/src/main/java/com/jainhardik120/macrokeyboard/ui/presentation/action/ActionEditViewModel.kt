package com.jainhardik120.macrokeyboard.ui.presentation.action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainhardik120.macrokeyboard.data.local.entity.ActionEntity
import com.jainhardik120.macrokeyboard.domain.model.Action
import com.jainhardik120.macrokeyboard.domain.repository.MacroRepository
import com.jainhardik120.macrokeyboard.ui.presentation.edit.EditButtonScreenEvent
import com.jainhardik120.macrokeyboard.ui.presentation.edit.EditButtonState
import com.jainhardik120.macrokeyboard.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActionEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MacroRepository
) : ViewModel()  {
    private val TAG = "ActionEditViewModel"
    var state by mutableStateOf(ActionEditState())

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    init {
        viewModelScope.launch {
            val sno = savedStateHandle.get<String>("sno")!!
            val childId = savedStateHandle.get<String>("childId")!!
            val initData = repository.getAction(childId.toInt(), sno.toInt())
            if(initData==null){
                state = state.copy(id = childId.toInt(), sno = sno.toInt())
            }else{
                state = state.copy(id = childId.toInt(), sno = sno.toInt(),actionType = initData.type, data = initData.data)
            }
        }
    }

    fun onEvent(event: ActionEditScreenEvent) {
        when (event) {
            is ActionEditScreenEvent.ButtonSaveClicked -> {
                viewModelScope.launch {
                    repository.addAction(ActionEntity(state.id, state.sno, state.actionType, state.data))
                    sendUiEvent(UiEvent.Navigate())
                }
            }
            is ActionEditScreenEvent.ActionDataChanged -> {
                state = state.copy(data = event.string)
            }
            is ActionEditScreenEvent.ActionTypeChanged -> {
                state = if (event.string.isNotEmpty()){
                    state.copy(actionType = event.string.toInt())
                }else{
                    state.copy(actionType = -1)
                }
            }
            else -> {}
        }
    }
}
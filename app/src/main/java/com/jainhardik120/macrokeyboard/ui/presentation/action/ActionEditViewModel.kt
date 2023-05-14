package com.jainhardik120.macrokeyboard.ui.presentation.action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainhardik120.macrokeyboard.data.local.entity.ActionEntity
import com.jainhardik120.macrokeyboard.domain.repository.MacroRepository
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
            state = if(initData==null){
                state.copy(id = childId.toInt(), sno = sno.toInt(), actionType = 1, stringData = "")
            }else{
                when(initData.type){
                    1->{
                        state = state.copy(stringData = initData.data)
                    }
                    2 -> {

                    }
                    3 -> {
                        val arr = initData.data.split(",")
                        state = state.copy(xCoordinate = arr[0], yCoordinate = arr[1])
                    }
                    4 -> {
                        state = state.copy(mouseButton = initData.data)
                    }
                    5 -> {
                        state = state.copy(delayMilliSeconds = initData.data)
                    }
                }
                state.copy(id = childId.toInt(), sno = sno.toInt(),actionType = initData.type)
            }
        }
    }

    fun onEvent(event: ActionEditScreenEvent) {
        when (event) {
            is ActionEditScreenEvent.ButtonSaveClicked -> {
                viewModelScope.launch {
                    var data = ""
                    when(state.actionType){
                        1->{
                            data = state.stringData
                        }
                        2 -> {

                        }
                        3 -> {
                            data = "${state.xCoordinate},${state.yCoordinate}"
                        }
                        4 -> {
                            data = state.mouseButton
                        }
                        5 -> {
                            data= state.delayMilliSeconds
                        }
                    }
                    repository.addAction(ActionEntity(state.id, state.sno, state.actionType, data))
                    sendUiEvent(UiEvent.Navigate())
                }
            }
            is ActionEditScreenEvent.StringActionDataChanged -> {
                state = state.copy(stringData = event.string)
            }
            is ActionEditScreenEvent.ActionTypeChanged -> {
                state = if (event.string.isNotEmpty()){
                    state.copy(actionType = event.string.toInt())
                }else{
                    state.copy(actionType = -1)
                }
            }
            is ActionEditScreenEvent.BackPressed -> {

            }

            is ActionEditScreenEvent.MouseXChanged -> {
                state = state.copy(xCoordinate = event.x)
            }
            is ActionEditScreenEvent.MouseYChanged -> {
                state = state.copy(yCoordinate = event.y)
            }
            is ActionEditScreenEvent.DelayChanged -> {
                state = state.copy(delayMilliSeconds = event.delay)
            }
            is ActionEditScreenEvent.MouseKeyChanged -> {
                state = state.copy(mouseButton = event.key)
            }
        }
    }
}
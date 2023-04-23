package com.jainhardik120.macrokeyboard.ui.presentation.edit

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainhardik120.macrokeyboard.data.local.entity.ActionEntity
import com.jainhardik120.macrokeyboard.data.local.entity.ScreenEntity
import com.jainhardik120.macrokeyboard.domain.model.Action
import com.jainhardik120.macrokeyboard.domain.repository.MacroRepository
import com.jainhardik120.macrokeyboard.ui.presentation.settings.SettingsState
import com.jainhardik120.macrokeyboard.util.Screen
import com.jainhardik120.macrokeyboard.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            val screenId = savedStateHandle.get<String>("screenId")!!
            val childId = savedStateHandle.get<String>("childId")!!
            if (childId == "-1") {
                state = state.copy(screenId = screenId, newButton = true)
            } else {
                repository.getButtonDetails(
                    childId = childId.toInt()
                )?.let { button ->
                    state = state.copy(
                        screenId = screenId, childId = childId,
                        label = button.label,
                        type = button.type,
                        newButton = false
                    )
                }
                repository.getActions(childId.toInt()).collect {
                    state = state.copy(list = it)
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }


    fun onEvent(event: EditButtonScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is EditButtonScreenEvent.SaveButtonClick -> {
                    if (state.childId.isNotEmpty()) {
                        if (state.label.isNotEmpty()) {
                            repository.addButton(
                                ScreenEntity(
                                    parentId = state.screenId.toInt(),
                                    childId = state.childId.toInt(),
                                    label = state.label,
                                    type = if (state.newButton) {
                                        if (state.list.isNotEmpty()) {
                                            1
                                        } else {
                                            0
                                        }
                                    } else {
                                        state.type
                                    }
                                )
                            )
                            sendUiEvent(UiEvent.Navigate())
                        } else {
                            sendUiEvent(UiEvent.ShowSnackbar("Name can't be Empty"))
                        }
                    } else {
                        if (state.label.isNotEmpty()) {
                            val childId = repository.addButton(
                                ScreenEntity(
                                    parentId = state.screenId.toInt(),
                                    label = state.label,
                                    type = state.type
                                )
                            )
                            state = state.copy(childId = childId.toString())
                            repository.getActions(childId.toInt()).collect {
                                state = state.copy(list = it)
                            }
                        } else {
                            sendUiEvent(UiEvent.ShowSnackbar("Name can't be Empty"))
                        }
                    }
                }
                is EditButtonScreenEvent.NewActionButtonClick -> {
                    sendUiEvent(
                        UiEvent.Navigate(
                            Screen.EditActionScreen.withArgs(
                                state.childId,
                                (state.list.size + 1).toString()
                            )
                        )
                    )
                }
                is EditButtonScreenEvent.ButtonNameChanged -> {
                    state = state.copy(label = event.string)
                }
                is EditButtonScreenEvent.EditButtonClicked -> {
                    sendUiEvent(
                        UiEvent.Navigate(
                            Screen.EditActionScreen.withArgs(
                                state.childId,
                                (state.list[event.id].sno).toString()
                            )
                        )
                    )
                }
                else -> {}
            }
        }
    }
}
package com.jainhardik120.macrokeyboard.ui.presentation.action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainhardik120.macrokeyboard.data.local.entity.ActionEntity
import com.jainhardik120.macrokeyboard.domain.repository.MacroRepository
import com.jainhardik120.macrokeyboard.util.Actions
import com.jainhardik120.macrokeyboard.util.UiEvent
import com.jainhardik120.macrokeyboard.util.actionFromJson
import com.jainhardik120.macrokeyboard.util.keyMaps
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActionEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MacroRepository
) : ViewModel() {
    var state by mutableStateOf(ActionEditState())

    private val map = keyMaps()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _searchResults = MutableStateFlow(map.keys.toList())

    val searchResult = searchText.combine(_searchResults) { text, results ->
        if (text.isBlank()) {
            results
        } else {
            results.filter {
                it.contains(text, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _searchResults.value)

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onSearchTextChanged(text:String){
        _searchText.value = text
    }

    fun onKeyItemSelected(text:String){
        val newList = List((state.action as Actions.KeyCombos).combos.size+1){
            if(it<(state.action as Actions.KeyCombos).combos.size){
                (state.action as Actions.KeyCombos).combos[it]
            }else{
                Pair(map[text]?:0, text)
            }
        }
        state = state.copy(action = Actions.KeyCombos(combos = newList))
    }

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
            if (initData == null) {
                state = state.copy(id = childId.toInt(), sno = sno.toInt(), isNewAction = true)
            } else {
                val action = actionFromJson(initData.type, initData.data)
                if(action!=null){
                    state = state.copy(id = childId.toInt(), sno = sno.toInt(), action = action)
                }
            }
        }
    }

    fun onEvent(event: ActionEditScreenEvent) {
        when (event) {
            is ActionEditScreenEvent.ButtonSaveClicked -> {
                viewModelScope.launch {
                    repository.addAction(
                        ActionEntity(
                            state.id,
                            state.sno,
                            state.action.actionTypeCode(),
                            state.action.toJsonString()
                        )
                    )
                    sendUiEvent(UiEvent.Navigate())
                }
            }

            is ActionEditScreenEvent.StringActionDataChanged -> {
                state = state.copy(action = Actions.StringInput(event.string))
            }

            is ActionEditScreenEvent.ActionTypeChanged -> {
                state = state.copy(action = event.action)
            }

            is ActionEditScreenEvent.BackPressed -> {
                if(state.isNewAction){
                    sendUiEvent(UiEvent.Navigate())
                }
            }

            is ActionEditScreenEvent.MouseXChanged -> {
                state = state.copy(action = Actions.MouseMove(x = event.x.toInt(), y = (state.action as Actions.MouseMove).y))
            }

            is ActionEditScreenEvent.MouseYChanged -> {
                state = state.copy(action = Actions.MouseMove(y = event.y.toInt(), x = (state.action as Actions.MouseMove).x))
            }

            is ActionEditScreenEvent.DelayChanged -> {
                state = state.copy(action = Actions.Delay(delay = event.delay.toInt()))
            }

            is ActionEditScreenEvent.MouseKeyChanged -> {
                state = state.copy(action = Actions.MouseClick(event.key.toInt()))
            }
        }
    }
}
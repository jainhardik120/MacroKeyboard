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
import org.json.JSONArray
import org.json.JSONObject
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
        val newList = List(state.keyComboArray.size+1){
            if(it<state.keyComboArray.size){
                state.keyComboArray[it]
            }else{
                Pair(map[text]?:0, text)
            }
        }
        state = state.copy(keyComboArray = newList)
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
            state = if (initData == null) {
                state.copy(id = childId.toInt(), sno = sno.toInt(), actionType = 1, stringData = "")
            } else {
                val jsonObject = JSONObject(initData.data)
                when (initData.type) {
                    1 -> {
                        state = state.copy(stringData = jsonObject.getString("string"))
                    }

                    2 -> {
                        val array = jsonObject.getJSONArray("keys")
                        val list = List(array.length()) {
                            val keyName = array.getJSONObject(it).getString("keyName") ?: ""
                            Pair(map[keyName] ?: 0, keyName)
                        }
                        state = state.copy(keyComboArray = list)
                    }

                    3 -> {
                        state = state.copy(
                            xCoordinate = jsonObject.getInt("x").toString(),
                            yCoordinate = jsonObject.getInt("y").toString()
                        )
                    }

                    4 -> {
                        state = state.copy(mouseButton = jsonObject.getString("button"))
                    }

                    5 -> {
                        state =
                            state.copy(delayMilliSeconds = jsonObject.getInt("delay").toString())
                    }
                }
                state.copy(id = childId.toInt(), sno = sno.toInt(), actionType = initData.type)
            }
        }
    }

    fun onEvent(event: ActionEditScreenEvent) {
        when (event) {
            is ActionEditScreenEvent.ButtonSaveClicked -> {
                viewModelScope.launch {
                    val jsonObject = JSONObject()
                    when (state.actionType) {
                        1 -> {
                            jsonObject.put("string", state.stringData)
                        }

                        2 -> {
                            val jsonArray = JSONArray()
                            for (i in state.keyComboArray) {
                                val tempObject = JSONObject()
                                tempObject.put("key", i.first)
                                tempObject.put("keyName", i.second)
                                jsonArray.put(tempObject)
                            }
                            jsonObject.put("keys", jsonArray)
                        }

                        3 -> {
                            jsonObject.put("x", state.xCoordinate.toInt())
                            jsonObject.put("y", state.yCoordinate.toInt())
                        }

                        4 -> {

                        }

                        5 -> {
                            jsonObject.put("delay", state.delayMilliSeconds.toInt())
                        }
                    }
                    repository.addAction(
                        ActionEntity(
                            state.id,
                            state.sno,
                            state.actionType,
                            jsonObject.toString()
                        )
                    )
                    sendUiEvent(UiEvent.Navigate())
                }
            }

            is ActionEditScreenEvent.StringActionDataChanged -> {
                state = state.copy(stringData = event.string)
            }

            is ActionEditScreenEvent.ActionTypeChanged -> {
                state = if (event.string.isNotEmpty()) {
                    state.copy(actionType = event.string.toInt())
                } else {
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
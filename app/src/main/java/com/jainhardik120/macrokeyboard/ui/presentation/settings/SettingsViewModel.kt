package com.jainhardik120.macrokeyboard.ui.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jainhardik120.macrokeyboard.domain.repository.MacroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: MacroRepository
) : ViewModel() {
    private val TAG = "SettingsViewModel"
    var state by mutableStateOf(SettingsState())

    fun ipaddressUpdated(ipAddress: String){
        state = state.copy(ipAddress = ipAddress)

    }

    fun portUpdated(port: String){
        state = state.copy(port = port)
    }
}
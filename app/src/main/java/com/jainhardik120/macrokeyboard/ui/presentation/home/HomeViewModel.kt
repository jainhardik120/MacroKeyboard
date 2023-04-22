package com.jainhardik120.macrokeyboard.ui.presentation.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainhardik120.macrokeyboard.domain.repository.MacroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    fun onEvent(event:HomeScreenEvent){
        when(event){
            is HomeScreenEvent.OnButtonClicked->{
                Log.d(TAG, "onEvent: Button Clicked")
                viewModelScope.launch {
                    sendData("192.168.10.104", 9155, "Hello")
                }
            }
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
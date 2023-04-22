package com.jainhardik120.macrokeyboard.ui.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jainhardik120.macrokeyboard.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigate: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.Navigate -> {
                    onNavigate()
                }
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }
    Scaffold(snackbarHost = {SnackbarHost(hostState = snackbarHostState)}) {
        Column(Modifier.fillMaxSize().padding(it),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = viewModel.state.ipAddress,
                onValueChange = {
                    viewModel.onEvent(SettingsScreenEvent.ipAddressChanged(it))
                },
                label = {
                    Text(text = "IP Address")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            OutlinedTextField(
                value = viewModel.state.port,
                onValueChange = {
                    viewModel.onEvent(SettingsScreenEvent.portChanged(it))
                },
                label = {
                    Text(text = "Port")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Button(onClick = {viewModel.onEvent(SettingsScreenEvent.onSaveButtonClicked)}) {
                Text(text = "Save")
            }
        }
    }

}
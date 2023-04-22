package com.jainhardik120.macrokeyboard.ui.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Column(Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
        ) {
        OutlinedTextField(
            value = viewModel.state.ipAddress,
            onValueChange = {
                viewModel.ipaddressUpdated(it)
            },
            label = {
                Text(text = "IP Address")
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
        OutlinedTextField(
            value = viewModel.state.port,
            onValueChange = {
                viewModel.portUpdated(it)
            },
            label = {
                Text(text = "Port")
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
    }
}
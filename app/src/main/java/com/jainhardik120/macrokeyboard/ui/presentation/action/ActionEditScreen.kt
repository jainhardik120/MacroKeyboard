package com.jainhardik120.macrokeyboard.ui.presentation.action

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jainhardik120.macrokeyboard.ui.presentation.edit.EditButtonScreenEvent
import com.jainhardik120.macrokeyboard.ui.presentation.edit.EditScreenViewModel
import com.jainhardik120.macrokeyboard.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionEditScreen(onNavigate: () -> Unit, viewModel: ActionEditViewModel = hiltViewModel()) {
    val state = viewModel.state
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
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            OutlinedTextField(
                value = if (state.actionType == -1) "" else state.actionType.toString(),
                onValueChange = { string ->
                    viewModel.onEvent(
                        ActionEditScreenEvent.ActionTypeChanged(
                            string
                        )
                    )
                },
                label = {
                    Text(text = "Action Type")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.data,
                onValueChange = { string ->
                    viewModel.onEvent(
                        ActionEditScreenEvent.ActionDataChanged(string)
                    )
                },
                label = {
                    Text(text = "Data")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}
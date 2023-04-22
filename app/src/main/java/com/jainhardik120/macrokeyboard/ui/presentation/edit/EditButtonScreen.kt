package com.jainhardik120.macrokeyboard.ui.presentation.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.jainhardik120.macrokeyboard.ui.presentation.settings.SettingsScreenEvent
import com.jainhardik120.macrokeyboard.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditButtonScreen(onNavigate: () -> Unit, viewModel: EditScreenViewModel = hiltViewModel()) {
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
                value = state.label,
                onValueChange = {

                },
                label = {
                    Text(text = "Button Name")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row() {
                Button(
                    onClick = { viewModel.onEvent(EditButtonScreenEvent.NewActionButtonClick) }
                ) {
                    Text(text = "Add New Action")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(
                    onClick = { viewModel.onEvent(EditButtonScreenEvent.SaveButtonClick) }
                ) {
                    Text(text = "Save")
                }
            }
            LazyColumn {
                items(state.actions.size) { index ->
                    ElevatedCard(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = state.actions[index].type.toString(),
                            onValueChange = {

                            },
                            label = {
                                Text(text = "Button Name")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = state.actions[index].data,
                            onValueChange = {

                            },
                            label = {
                                Text(text = "Button Name")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }

        }
    }
}
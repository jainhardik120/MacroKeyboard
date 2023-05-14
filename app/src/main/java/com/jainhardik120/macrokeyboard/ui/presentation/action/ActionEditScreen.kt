package com.jainhardik120.macrokeyboard.ui.presentation.action

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jainhardik120.macrokeyboard.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionEditScreen(onNavigate: () -> Unit, viewModel: ActionEditViewModel = hiltViewModel()) {
    val state = viewModel.state
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }
    val list = listOf<String>("String Input", "Key Combo", "Mouse Move", "Mouse Click", "Delay")
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
    BackHandler(enabled = true) {
        viewModel.onEvent(ActionEditScreenEvent.BackPressed)
    }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = list[state.actionType - 1],
                    onValueChange = {

                    },
                    modifier = Modifier.menuAnchor(),
                    label = { Text(text = "Action Type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    readOnly = true
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = {
                    expanded = false
                }) {
                    list.forEachIndexed { index, s ->
                        DropdownMenuItem(
                            text = { Text(text = s) },
                            onClick = {
                                viewModel.onEvent(ActionEditScreenEvent.ActionTypeChanged((index + 1).toString()))
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            when (state.actionType) {
                1 -> {
                    OutlinedTextField(
                        value = state.stringData,
                        onValueChange = { string ->
                            viewModel.onEvent(
                                ActionEditScreenEvent.StringActionDataChanged(string)
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
                2 -> {

                }
                3 -> {
                    Row {
                        OutlinedTextField(value = state.xCoordinate, onValueChange = {
                            viewModel.onEvent(ActionEditScreenEvent.MouseXChanged(it))
                        })
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(value = state.yCoordinate, onValueChange = {
                            viewModel.onEvent(ActionEditScreenEvent.MouseYChanged(it))
                        })
                    }
                }
                4 -> {

                }
                5 -> {
                    OutlinedTextField(
                        value = state.delayMilliSeconds,
                        onValueChange = { string ->
                            viewModel.onEvent(
                                ActionEditScreenEvent.DelayChanged(string)
                            )
                        },
                        label = {
                            Text(text = "Delay (in milliSeconds)")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                else -> {

                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                viewModel.onEvent(ActionEditScreenEvent.ButtonSaveClicked)
            }) {
                Text(text = "Save")
            }
        }
    }
}
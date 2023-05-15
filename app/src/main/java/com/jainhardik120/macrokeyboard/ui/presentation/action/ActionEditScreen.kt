package com.jainhardik120.macrokeyboard.ui.presentation.action

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jainhardik120.macrokeyboard.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ActionEditScreen(onNavigate: () -> Unit, viewModel: ActionEditViewModel = hiltViewModel()) {
    val state = viewModel.state
    val hostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }
    val list = listOf("String Input", "Key Combo", "Mouse Move", "Mouse Click", "Delay")
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.Navigate -> {
                    onNavigate()
                }

                is UiEvent.ShowSnackbar -> {
                    hostState.showSnackbar(it.message)
                }
            }
        }
    }
    BackHandler(enabled = true) {
        viewModel.onEvent(ActionEditScreenEvent.BackPressed)
    }
    Scaffold(snackbarHost = { SnackbarHost(hostState = hostState) }) {
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
            Button(onClick = {
                viewModel.onEvent(ActionEditScreenEvent.ButtonSaveClicked)
            }) {
                Text(text = "Save")
            }
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
                    val searchText by viewModel.searchText.collectAsState()
                    val searchResults by viewModel.searchResult.collectAsState()
                    FlowRow(Modifier.fillMaxWidth()) {
                        for (i in state.keyComboArray){
                            InputChip(selected = false, onClick = {

                            }, label = {
                                Text(text = i.second)
                            }, trailingIcon = {
                                Icon(Icons.Filled.Close, contentDescription = "Close Icon")
                            })
                        }
                    }
                    Column {
                        TextField(value = searchText, onValueChange = viewModel::onSearchTextChanged)
                        LazyColumn(content = {
                            itemsIndexed(searchResults){index, item ->  
                                Text(text = item, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                                    .clickable(
                                        onClick = { viewModel.onKeyItemSelected(item) }
                                    ))
                            }
                        })
                    }
                }
                3 -> {
                    Row {
                        OutlinedTextField(value = state.xCoordinate, onValueChange = {string->
                            viewModel.onEvent(ActionEditScreenEvent.MouseXChanged(string))
                        })
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(value = state.yCoordinate, onValueChange = {string->
                            viewModel.onEvent(ActionEditScreenEvent.MouseYChanged(string))
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

        }
    }
}
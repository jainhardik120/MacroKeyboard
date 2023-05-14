package com.jainhardik120.macrokeyboard.ui.presentation.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jainhardik120.macrokeyboard.util.UiEvent

@Composable
fun EditButtonScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: EditScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val hostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.Navigate -> {
                    onNavigate(it)
                }
                is UiEvent.ShowSnackbar -> {
                    hostState.showSnackbar(it.message)
                }
            }
        }
    }
    BackHandler(enabled = true) {
        viewModel.onEvent(EditButtonScreenEvent.BackPressed)
    }
    Scaffold(snackbarHost = { SnackbarHost(hostState = hostState) }) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            OutlinedTextField(
                value = state.label,
                onValueChange = { string ->
                    viewModel.onEvent(EditButtonScreenEvent.ButtonNameChanged(string))
                },
                label = {
                    Text(text = "Button Name")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                if((state.newButton && state.childId.isNotEmpty())||(!state.newButton && state.type==1)){
                    Button(
                        onClick = { viewModel.onEvent(EditButtonScreenEvent.NewActionButtonClick) }
                    ) {
                        Text(text = "Add New Action")
                    }
                }
                Button(
                    onClick = { viewModel.onEvent(EditButtonScreenEvent.SaveButtonClick) }
                ) {
                    if (state.childId.isNotEmpty()) {
                        Text(text = "Save Button")
                    } else {
                        Text(text = "Create Button")
                    }
                }
                Button(onClick = {viewModel.onEvent(EditButtonScreenEvent.DeleteClicked)}){
                    if(state.newButton){
                        Text(text = "Cancel")
                    }else{
                        Text(text = "Delete Button")
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            LazyColumn {
                items(state.list.size) {
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Row{
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = state.list[it].type.toString())
                                Text(text = state.list[it].data)
                            }
                            IconButton(onClick = {
                                viewModel.onEvent(
                                    EditButtonScreenEvent.EditButtonClicked(
                                        it
                                    )
                                )
                            }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Edit Button")
                            }
                        }


                    }
                }
            }

        }
    }
}
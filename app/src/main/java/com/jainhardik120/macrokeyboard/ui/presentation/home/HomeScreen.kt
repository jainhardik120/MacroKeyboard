package com.jainhardik120.macrokeyboard.ui.presentation.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jainhardik120.macrokeyboard.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(onNavigate: (UiEvent.Navigate) -> Unit, viewModel: HomeViewModel = hiltViewModel()) {
    val TAG = "HomeScreen"
    val state = viewModel.state
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.Navigate -> {
                    onNavigate(it)
                }
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }
    BackHandler(enabled = true) {
        viewModel.onEvent(HomeScreenEvent.BackPressed)
    }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Button(onClick = { viewModel.onEvent(HomeScreenEvent.OnSettingsButtonClicked) }) {
                Text(text = "Settings")
            }
            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 48.dp)) {
                items(viewModel.state.screenEntities.size + 1) { it ->
                    androidx.compose.material.Surface(
                        modifier = Modifier.combinedClickable(
                            onClick = {
                                viewModel.onEvent(HomeScreenEvent.OnButtonClicked(it))
                            },
                            enabled = true,
                            onLongClick = {
                                viewModel.onEvent(HomeScreenEvent.OnButtonLongClicked(it))
                            }
                        )
                    ) {
                        Column(Modifier.size(28.dp)) {
                            Text(text = if (it == state.screenEntities.size) "New" else state.screenEntities[it].label)
                        }
                    }
                }
            }
        }
    }
}
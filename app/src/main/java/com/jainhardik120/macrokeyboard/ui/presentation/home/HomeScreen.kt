package com.jainhardik120.macrokeyboard.ui.presentation.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jainhardik120.macrokeyboard.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(onNavigate: (UiEvent.Navigate) -> Unit, viewModel: HomeViewModel = hiltViewModel()) {
    val TAG = "HomeScreen"
    val state = viewModel.state
    val screenInfo = viewModel.screenInfo.collectAsState(initial = emptyList())
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
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text("Macro Keys", maxLines = 1, overflow = TextOverflow.Ellipsis)
            },actions = {
                IconButton(onClick = { viewModel.onEvent(HomeScreenEvent.OnSettingsButtonClicked) }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        )
    }, snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 96.dp)) {
                items(screenInfo.value.size+1) { index ->
                    if(index==screenInfo.value.size){
                        androidx.compose.material.Surface(
                            modifier = Modifier.combinedClickable(
                                onClick = {
                                    viewModel.onEvent(HomeScreenEvent.OnNewButtonClicked)
                                },
                                enabled = true
                            ).padding(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Column(modifier = Modifier.size(96.dp), verticalArrangement = Arrangement.SpaceAround) {
                                Text(text = "New", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                            }
                        }
                    }else{
                        androidx.compose.material.Surface(
                            modifier = Modifier.combinedClickable(
                                onClick = {
                                    viewModel.onEvent(HomeScreenEvent.OnButtonClicked(screenInfo.value[index]))
                                },
                                enabled = true,
                                onLongClick = {
                                    viewModel.onEvent(HomeScreenEvent.OnButtonLongClicked(screenInfo.value[index]))
                                }
                            ).padding(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Column(modifier = Modifier.size(96.dp), verticalArrangement = Arrangement.SpaceAround) {
                                Text(text = screenInfo.value[index].label, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                            }
                        }
                    }
                }

            }

        }
    }
}
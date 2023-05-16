package com.jainhardik120.macrokeyboard.ui.presentation.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jainhardik120.macrokeyboard.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigate: (UiEvent.Navigate) -> Unit, viewModel: HomeViewModel = hiltViewModel()) {
    val state = viewModel.state
    val screenInfo = viewModel.screenInfo.collectAsState(initial = emptyList())
    val hostState = remember { SnackbarHostState() }
    val haptic = LocalHapticFeedback.current
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
        viewModel.onEvent(HomeScreenEvent.BackPressed)
    }
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text("Macro Keys", maxLines = 1, overflow = TextOverflow.Ellipsis)
            }, actions = {
                IconButton(onClick = { viewModel.onEvent(HomeScreenEvent.OnSettingsButtonClicked) }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings"
                    )
                }
                IconButton(onClick = {
                    viewModel.onEvent(HomeScreenEvent.CloseClicked)
                }) {
                    Icon(
                        if (!state.connectedState) {
                            Icons.Filled.Refresh
                        } else {
                            Icons.Filled.Close
                        },
                        contentDescription = "Close Icon"
                    )
                }
            }
        )
    }, snackbarHost = { SnackbarHost(hostState = hostState) }) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 96.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                items(screenInfo.value.size + 1) { index ->
                    if (index == screenInfo.value.size) {
                        ActionButton(
                            text = "New",
                            onClick = {
                                viewModel.onEvent(HomeScreenEvent.OnNewButtonClicked)
                            },
                            onLongClick = {

                            }
                        )
                    } else {
                        ActionButton(
                            text = screenInfo.value[index].label,
                            onClick = {
                                viewModel.onEvent(HomeScreenEvent.OnButtonClicked(screenInfo.value[index]))
                            },
                            onLongClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.onEvent(
                                    HomeScreenEvent.OnButtonLongClicked(
                                        screenInfo.value[index]
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    shape: Shape = MaterialTheme.shapes.large,
    border: BorderStroke? = null
) {
    val containerColor = MaterialTheme.colorScheme.secondaryContainer
    val contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    Surface(
        modifier = modifier
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onLongClick() },
                    onTap = { onClick() }
                )
            },
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        border = border
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
                Column(
                    Modifier
                        .size(96.dp)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        Text(text = text, textAlign = TextAlign.Center)
                    }
                )
            }
        }
    }
}
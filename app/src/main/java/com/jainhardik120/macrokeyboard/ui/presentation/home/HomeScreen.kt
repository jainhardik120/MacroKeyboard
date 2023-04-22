package com.jainhardik120.macrokeyboard.ui.presentation.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    Button(onClick = {
        viewModel.onEvent(HomeScreenEvent.OnButtonClicked)
    }) {
        Text(text = "Click Me")
    }
}
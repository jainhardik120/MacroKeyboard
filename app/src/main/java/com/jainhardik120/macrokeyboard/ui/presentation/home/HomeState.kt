package com.jainhardik120.macrokeyboard.ui.presentation.home

data class HomeState(
    val currentScreen: List<Int> = listOf(0),
    val connectedState:Boolean = false
)
package com.jainhardik120.macrokeyboard.ui.presentation.home

import com.jainhardik120.macrokeyboard.data.local.entity.ScreenEntity

data class HomeState(
    val currentScreen: List<Int> = listOf(0),
    val connectedState:Boolean = false
)
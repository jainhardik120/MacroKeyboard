package com.jainhardik120.macrokeyboard.ui.presentation.home

import com.jainhardik120.macrokeyboard.data.local.entity.ScreenEntity

data class HomeState(
    val currentScreen: Int = 0,
    val screenEntities: List<ScreenEntity> = emptyList()
)
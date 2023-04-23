package com.jainhardik120.macrokeyboard.ui.presentation.home

import com.jainhardik120.macrokeyboard.data.local.entity.ScreenEntity

sealed class HomeScreenEvent{
    object OnSettingsButtonClicked:HomeScreenEvent()
    data class OnButtonLongClicked(val screenEntity: ScreenEntity):HomeScreenEvent()
    data class OnButtonClicked(val screenEntity: ScreenEntity):HomeScreenEvent()
    object BackPressed:HomeScreenEvent()
    object OnNewButtonClicked:HomeScreenEvent()
}

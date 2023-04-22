package com.jainhardik120.macrokeyboard.ui.presentation.home

sealed class HomeScreenEvent{
    object OnSettingsButtonClicked:HomeScreenEvent()
    data class OnButtonLongClicked(val id: Int):HomeScreenEvent()
    data class OnButtonClicked(val id: Int):HomeScreenEvent()
    object BackPressed:HomeScreenEvent()
}

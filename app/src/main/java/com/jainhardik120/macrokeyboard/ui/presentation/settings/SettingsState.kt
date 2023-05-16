package com.jainhardik120.macrokeyboard.ui.presentation.settings

data class SettingsState(
    val ipAddress : String = "",
    val port: String = "",
    val darkMode: Boolean = false,
    val dynamicColors: Boolean = false
)
package com.jainhardik120.macrokeyboard.ui.presentation.settings

sealed class SettingsScreenEvent{
    object onSaveButtonClicked:SettingsScreenEvent()
    object OnCancelButtonClicked:SettingsScreenEvent()
    data class ipAddressChanged(val ipAddress : String):SettingsScreenEvent()
    data class portChanged(val port : String):SettingsScreenEvent()
    data class DarkModeToggled(val value: Boolean):SettingsScreenEvent()
    data class DynamicColorsToggled(val value: Boolean):SettingsScreenEvent()
    object BackPressed: SettingsScreenEvent()
}

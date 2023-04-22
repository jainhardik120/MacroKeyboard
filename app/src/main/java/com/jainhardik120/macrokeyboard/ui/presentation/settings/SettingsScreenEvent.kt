package com.jainhardik120.macrokeyboard.ui.presentation.settings

sealed class SettingsScreenEvent{
    object onSaveButtonClicked:SettingsScreenEvent()
    data class ipAddressChanged(val ipAddress : String):SettingsScreenEvent()
    data class portChanged(val port : String):SettingsScreenEvent()
}

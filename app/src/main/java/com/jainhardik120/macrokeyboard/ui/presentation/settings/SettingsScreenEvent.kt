package com.jainhardik120.macrokeyboard.ui.presentation.settings

import com.jainhardik120.macrokeyboard.ui.presentation.home.HomeScreenEvent

sealed class SettingsScreenEvent{
    object onSaveButtonClicked:SettingsScreenEvent()
    data class ipAddressChanged(val ipAddress : String):SettingsScreenEvent()
    data class portChanged(val port : String):SettingsScreenEvent()
    object BackPressed: SettingsScreenEvent()
}

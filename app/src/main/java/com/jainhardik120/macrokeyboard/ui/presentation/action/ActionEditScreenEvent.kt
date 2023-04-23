package com.jainhardik120.macrokeyboard.ui.presentation.action

import com.jainhardik120.macrokeyboard.ui.presentation.home.HomeScreenEvent

sealed class ActionEditScreenEvent {
    object ButtonSaveClicked : ActionEditScreenEvent()
    data class ActionTypeChanged(val string: String) : ActionEditScreenEvent()
    data class ActionDataChanged(val string: String) : ActionEditScreenEvent()
    object BackPressed: ActionEditScreenEvent()
}
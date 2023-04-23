package com.jainhardik120.macrokeyboard.ui.presentation.action

sealed class ActionEditScreenEvent {
    object ButtonSaveClicked : ActionEditScreenEvent()
    data class ActionTypeChanged(val string: String) : ActionEditScreenEvent()
    data class ActionDataChanged(val string: String) : ActionEditScreenEvent()
}
package com.jainhardik120.macrokeyboard.ui.presentation.action

sealed class ActionEditScreenEvent {
    object ButtonSaveClicked : ActionEditScreenEvent()
    data class ActionTypeChanged(val string: String) : ActionEditScreenEvent()
    data class StringActionDataChanged(val string: String) : ActionEditScreenEvent()
    data class MouseXChanged(val x:String):ActionEditScreenEvent()
    data class MouseYChanged(val y:String):ActionEditScreenEvent()
    data class MouseKeyChanged(val key:String):ActionEditScreenEvent()
    data class DelayChanged(val delay:String):ActionEditScreenEvent()
    object BackPressed: ActionEditScreenEvent()
}
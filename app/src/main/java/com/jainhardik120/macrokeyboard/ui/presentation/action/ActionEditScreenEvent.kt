package com.jainhardik120.macrokeyboard.ui.presentation.action

import com.jainhardik120.macrokeyboard.util.Actions

sealed class ActionEditScreenEvent {
    object ButtonSaveClicked : ActionEditScreenEvent()
    data class ActionTypeChanged(val action: Actions) : ActionEditScreenEvent()
    data class StringActionDataChanged(val string: String) : ActionEditScreenEvent()
    data class MouseXChanged(val x:String):ActionEditScreenEvent()
    data class MouseYChanged(val y:String):ActionEditScreenEvent()
    data class MouseKeyChanged(val key:String):ActionEditScreenEvent()
    data class DelayChanged(val delay:String):ActionEditScreenEvent()
    object BackPressed: ActionEditScreenEvent()
}
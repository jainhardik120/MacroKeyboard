package com.jainhardik120.macrokeyboard.ui.presentation.edit

sealed class EditButtonScreenEvent {
    object SaveButtonClick:EditButtonScreenEvent()
    object NewActionButtonClick:EditButtonScreenEvent()
    data class ButtonNameChanged(val string: String):EditButtonScreenEvent()
}
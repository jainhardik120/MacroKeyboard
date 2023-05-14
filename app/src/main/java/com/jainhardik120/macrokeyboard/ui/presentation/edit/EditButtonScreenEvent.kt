package com.jainhardik120.macrokeyboard.ui.presentation.edit

sealed class EditButtonScreenEvent {
    object SaveButtonClick:EditButtonScreenEvent()
    object NewActionButtonClick:EditButtonScreenEvent()
    data class ButtonNameChanged(val string: String):EditButtonScreenEvent()
    data class EditButtonClicked(val id: Int):EditButtonScreenEvent()
    object BackPressed: EditButtonScreenEvent()
    object DeleteClicked: EditButtonScreenEvent()
}
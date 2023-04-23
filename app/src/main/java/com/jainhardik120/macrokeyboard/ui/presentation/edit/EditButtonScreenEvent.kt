package com.jainhardik120.macrokeyboard.ui.presentation.edit

import com.jainhardik120.macrokeyboard.ui.presentation.home.HomeScreenEvent

sealed class EditButtonScreenEvent {
    object SaveButtonClick:EditButtonScreenEvent()
    object NewActionButtonClick:EditButtonScreenEvent()
    data class ButtonNameChanged(val string: String):EditButtonScreenEvent()
    data class EditButtonClicked(val id: Int):EditButtonScreenEvent()
    object BackPressed: EditButtonScreenEvent()
    object DeleteClicked: EditButtonScreenEvent()
}
package com.jainhardik120.macrokeyboard.util

sealed class UiEvent {
    data class Navigate(val route: String? = null) : UiEvent()
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ): UiEvent()
}
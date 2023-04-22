package com.jainhardik120.macrokeyboard.ui.presentation.edit

import com.jainhardik120.macrokeyboard.domain.model.Action

data class EditButtonState(
    val screenId: String = "",
    val childId: String = "",
    val label: String = "",
    val type: Int = 0,
    val actions : List<Action> = listOf()
)
package com.jainhardik120.macrokeyboard.ui.presentation.action

data class ActionEditState(
    val id: Int = -1,
    val sno: Int = -1,
    val isNewAction:Boolean = false,
    val actionType: Int = 1,
    val stringData: String = "",
    val xCoordinate: String = "",
    val yCoordinate: String = "",
    val mouseButton: String = "",
    val delayMilliSeconds: String = "",
    val keyComboArray:List<Pair<Int, String>> = emptyList()
)

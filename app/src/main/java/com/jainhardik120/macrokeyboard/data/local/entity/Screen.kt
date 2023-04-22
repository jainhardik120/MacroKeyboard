package com.jainhardik120.macrokeyboard.data.local.entity

import androidx.room.Entity

@Entity(tableName = "screen_table", primaryKeys = arrayOf("parentId", "childId"))
data class ScreenEntity(
    val parentId:Int,
    val childId: Int,
    val label: String,
    val type : Int,
    val data: String
)

package com.jainhardik120.macrokeyboard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "screen_table")
data class ScreenEntity(
    val parentId:Int,
    @PrimaryKey(autoGenerate = true) val childId: Int? = null,
    val label: String,
    val type : Int = 0
)
package com.jainhardik120.macrokeyboard.data.local.entity

import androidx.room.Entity

@Entity(tableName = "actions_table", primaryKeys = ["id", "sno"])
data class ActionEntity(
    val id:Int,
    val sno:Int,
    val type:Int,
    val data:String
)

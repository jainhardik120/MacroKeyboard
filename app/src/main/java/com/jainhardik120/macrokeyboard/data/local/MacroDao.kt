package com.jainhardik120.macrokeyboard.data.local

import androidx.room.Dao
import androidx.room.Query
import com.jainhardik120.macrokeyboard.data.local.entity.ScreenEntity

@Dao
interface MacroDao {

    @Query("SELECT * FROM screen_table WHERE parentId = :id")
    suspend fun getScreenInfo(id: Int): List<ScreenEntity>

    @Query("SELECT * FROM screen_table WHERE parentId = :parentId and childId = :childId")
    suspend fun getButtonInfo(
        parentId: Int,
        childId: Int
    ): ScreenEntity? = null
}
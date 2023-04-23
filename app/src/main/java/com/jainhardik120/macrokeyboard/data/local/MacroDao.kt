package com.jainhardik120.macrokeyboard.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jainhardik120.macrokeyboard.data.local.entity.ActionEntity
import com.jainhardik120.macrokeyboard.data.local.entity.ScreenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MacroDao {

    @Query("SELECT * FROM screen_table WHERE parentId = :id")
     fun getScreenInfo(id: Int): Flow<List<ScreenEntity>>

    @Query("SELECT * FROM screen_table WHERE childId = :childId")
    suspend fun getButtonInfo(
        childId: Int
    ): ScreenEntity?

    @Query("SELECT * FROM actions_table WHERE sno = :sno and id = :childId")
    suspend fun getActionInfo(
        childId: Int,
        sno: Int
    ): ActionEntity?

    @Query("SELECT * FROM actions_table WHERE id = :id")
    suspend fun getActions(id:Int):List<ActionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAction(actionEntity: ActionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertButton(screenEntity: ScreenEntity):Long


}
package com.jainhardik120.macrokeyboard.domain.repository

import androidx.lifecycle.LiveData
import com.jainhardik120.macrokeyboard.data.local.entity.ActionEntity
import com.jainhardik120.macrokeyboard.data.local.entity.ScreenEntity
import com.jainhardik120.macrokeyboard.domain.model.Action
import com.jainhardik120.macrokeyboard.util.Resource
import kotlinx.coroutines.flow.Flow


interface MacroRepository {

     fun getScreen(id: Int) : Flow<List<ScreenEntity>>

    fun getConnectionInfo():Pair<String, Int>

    fun updatePortInfo(updatedValues: Pair<String, Int>)

    suspend fun getButtonDetails(childId: Int):ScreenEntity?

    suspend fun getButtonActions(id:Int):List<ActionEntity>

    suspend fun getAction(childId: Int, sno : Int):ActionEntity?

    suspend fun addAction(action:ActionEntity)
    suspend fun addButton(screen:ScreenEntity):Long
}
package com.jainhardik120.macrokeyboard.domain.repository

import com.jainhardik120.macrokeyboard.data.local.entity.ScreenEntity
import com.jainhardik120.macrokeyboard.util.Resource
import kotlinx.coroutines.flow.Flow


interface MacroRepository {

    fun getScreen(id: Int) : Flow<Resource<List<ScreenEntity>>>

    fun getConnectionInfo():Pair<String, Int>

    fun updatePortInfo(updatedValues: Pair<String, Int>)

    suspend fun getButtonDetails(screenId: Int, childId: Int):ScreenEntity?=null
}
package com.jainhardik120.macrokeyboard.data.repository

import android.content.SharedPreferences
import com.jainhardik120.macrokeyboard.data.local.MacroDatabase
import com.jainhardik120.macrokeyboard.data.local.entity.ScreenEntity
import com.jainhardik120.macrokeyboard.domain.repository.MacroRepository
import com.jainhardik120.macrokeyboard.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MacroRepositoryImpl @Inject constructor(
    private val db: MacroDatabase,
    private val sharedPreferences: SharedPreferences
) : MacroRepository{
    private val TAG = "MacroRepositoryDebug"
    private val dao = db.dao

    override fun getScreen(id: Int): Flow<Resource<List<ScreenEntity>>> =  flow {
        emit(Resource.Loading())
        val screenData = dao.getScreenInfo(id)
        emit(Resource.Success(screenData))
    }

    override fun getConnectionInfo(): Pair<String, Int> {
        return Pair(sharedPreferences.getString("ipAddress", "192.168.10.104")!!, sharedPreferences.getInt("port", 9155))
    }

    override fun updatePortInfo(updatedValues: Pair<String, Int>) {
        with(sharedPreferences.edit()){
            putString("ipAddress", updatedValues.first)
            putInt("port", updatedValues.second)
            apply()
        }
    }

    override suspend fun getButtonDetails(screenId: Int, childId: Int): ScreenEntity? {
        return dao.getButtonInfo(parentId = screenId, childId = childId)
    }
}
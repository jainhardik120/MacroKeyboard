package com.jainhardik120.macrokeyboard.data.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.jainhardik120.macrokeyboard.data.local.MacroDatabase
import com.jainhardik120.macrokeyboard.data.local.entity.ActionEntity
import com.jainhardik120.macrokeyboard.data.local.entity.ScreenEntity
import com.jainhardik120.macrokeyboard.domain.repository.MacroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MacroRepositoryImpl @Inject constructor(
    private val db: MacroDatabase,
    private val sharedPreferences: SharedPreferences
) : MacroRepository{
    private val TAG = "MacroRepositoryDebug"
    private val dao = db.dao


    override fun isDarkSetting(): Boolean {
        return sharedPreferences.getBoolean("DarkMode", true)
    }

    override fun isDynamicColors(): Boolean {
        return sharedPreferences.getBoolean("DynamicColors", false)
    }

    override fun updateDarkSetting(value: Boolean) {
        with(sharedPreferences.edit()){
            putBoolean("DarkMode", value)
            apply()
        }
    }

    override fun updateDynamicColors(value: Boolean) {
        with(sharedPreferences.edit()){
            putBoolean("DynamicColors", value)
            apply()
        }
    }

    override fun getScreen(id: Int): Flow<List<ScreenEntity>> {
        return dao.getScreenInfo(id)
    }

    override  fun getActions(id: Int): Flow<List<ActionEntity>> {
        Log.d(TAG, "getActions: Ran")
        return dao.getActions(id)
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

    override suspend fun deleteAction(id: Int, sno: Int) {
        dao.deleteAction(sno, id)
    }

    override suspend fun deleteButton(childId: Int) {
        dao.deleteButton(childId)
    }

    override suspend fun getButtonDetails(childId: Int): ScreenEntity? {
        return dao.getButtonInfo(childId = childId)
    }

    override suspend fun getButtonActions(id: Int): List<ActionEntity> {
        return dao.getActionsInfo(id)
    }

    override suspend fun addAction(action: ActionEntity) {
        dao.insertAction(action)
    }

    override suspend fun addButton(screen: ScreenEntity) : Long{
        return dao.insertButton(screen)
    }

    override suspend fun getAction(childId: Int, sno: Int): ActionEntity? {
        return dao.getActionInfo(childId, sno)
    }
}
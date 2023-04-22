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
}
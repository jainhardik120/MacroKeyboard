package com.jainhardik120.macrokeyboard.data.repository

import android.content.SharedPreferences
import com.jainhardik120.macrokeyboard.data.local.MacroDatabase
import com.jainhardik120.macrokeyboard.domain.repository.MacroRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MacroRepositoryImpl @Inject constructor(
    private val db: MacroDatabase,
    private val sharedPreferences: SharedPreferences
) : MacroRepository{
    private val TAG = "MacroRepositoryDebug"
    private val dao = db.dao
}
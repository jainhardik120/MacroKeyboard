package com.jainhardik120.macrokeyboard.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jainhardik120.macrokeyboard.data.local.entity.ScreenEntity

@Database(entities = [ScreenEntity::class], version = 1)
abstract class MacroDatabase : RoomDatabase(){
    abstract val dao: MacroDao
}
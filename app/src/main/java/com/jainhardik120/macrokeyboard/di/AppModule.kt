package com.jainhardik120.macrokeyboard.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.jainhardik120.macrokeyboard.data.local.MacroDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMacroDatabase(app:Application):MacroDatabase{
        return Room.databaseBuilder(app, MacroDatabase::class.java, "macro_database").fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences("sharedpreferences", Context.MODE_PRIVATE)
    }
}
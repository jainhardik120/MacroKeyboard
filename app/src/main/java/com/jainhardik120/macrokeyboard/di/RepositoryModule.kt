package com.jainhardik120.macrokeyboard.di

import com.jainhardik120.macrokeyboard.data.repository.MacroRepositoryImpl
import com.jainhardik120.macrokeyboard.domain.repository.MacroRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMacroRepository(
        macroRepositoryImpl: MacroRepositoryImpl
    ) : MacroRepository
}
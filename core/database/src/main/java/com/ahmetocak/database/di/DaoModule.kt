package com.ahmetocak.database.di

import com.ahmetocak.database.dao.WatchListDao
import com.ahmetocak.database.db.WatchListDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideWatchListDao(db: WatchListDatabase): WatchListDao {
        return db.watchListDao()
    }
}
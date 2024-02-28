package com.ahmetocak.database.di

import com.ahmetocak.database.dao.WatchListDao
import com.ahmetocak.database.datasource.WatchListLocalDataSource
import com.ahmetocak.database.datasource.WatchListLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatasourceModule {

    @Singleton
    @Provides
    fun provideWatchListLocalDataSource(dao: WatchListDao): WatchListLocalDataSource {
        return WatchListLocalDataSourceImpl(dao)
    }
}
package com.ahmetocak.movieapp.di

import android.content.Context
import androidx.room.Room
import com.ahmetocak.movieapp.data.datasource.local.watch_list.db.WatchListDao
import com.ahmetocak.movieapp.data.datasource.local.watch_list.db.WatchListDatabase
import com.ahmetocak.movieapp.utils.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideWatchListDatabase(@ApplicationContext context: Context): WatchListDatabase {
        return Room.databaseBuilder(
            context,
            WatchListDatabase::class.java,
            Database.WATCH_LIST_DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideWatchListDao(db: WatchListDatabase): WatchListDao {
        return db.watchListDao()
    }
}
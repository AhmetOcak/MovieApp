package com.ahmetocak.database.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.ahmetocak.database.db.WatchListDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWatchListDatabase(@ApplicationContext context: Context): WatchListDatabase {
        return Room.databaseBuilder(
            context,
            WatchListDatabase::class.java,
            "watch_list_db"
        ).build()
    }
}
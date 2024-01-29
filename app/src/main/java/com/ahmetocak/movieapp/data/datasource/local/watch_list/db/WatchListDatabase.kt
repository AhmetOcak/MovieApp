package com.ahmetocak.movieapp.data.datasource.local.watch_list.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmetocak.movieapp.model.watch_list.WatchListEntity

@Database(entities = [WatchListEntity::class], version = 1)
abstract class WatchListDatabase : RoomDatabase() {

    abstract fun watchListDao(): WatchListDao
}
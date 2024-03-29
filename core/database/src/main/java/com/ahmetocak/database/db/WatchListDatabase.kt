package com.ahmetocak.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmetocak.database.dao.WatchListDao
import com.ahmetocak.database.entity.WatchListEntity

@Database(entities = [WatchListEntity::class], version = 1)
abstract class WatchListDatabase : RoomDatabase() {

    abstract fun watchListDao(): WatchListDao
}
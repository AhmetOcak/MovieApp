package com.ahmetocak.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmetocak.database.entity.WatchListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieToWatchList(watchListEntity: WatchListEntity)

    @Query("SELECT * FROM WatchListEntity")
    fun getWatchList(): Flow<List<WatchListEntity>>

    @Query("DELETE FROM WatchListEntity WHERE id == :movieId")
    suspend fun removeMovieFromWatchList(movieId: Int)

    @Query("DELETE FROM WatchListEntity")
    suspend fun deleteWatchList()
}
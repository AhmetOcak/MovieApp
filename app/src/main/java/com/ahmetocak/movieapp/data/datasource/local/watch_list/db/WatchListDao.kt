package com.ahmetocak.movieapp.data.datasource.local.watch_list.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmetocak.movieapp.model.watch_list.WatchListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieToWatchList(watchListEntity: WatchListEntity)

    @Query("SELECT * FROM WatchListEntity")
    fun getWatchList(): Flow<List<WatchListEntity>>

    @Query("DELETE FROM WatchListEntity WHERE id == :movieId")
    suspend fun removeMovieFromWatchList(movieId: Int)
}
package com.ahmetocak.movieapp.data.datasource.local.watch_list.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ahmetocak.movieapp.model.watch_list.WatchListEntity

@Dao
interface WatchListDao {

    @Insert
    suspend fun addMovieToWatchList(watchListEntity: WatchListEntity)

    @Query("SELECT * FROM WatchListEntity")
    suspend fun getWatchList(): List<WatchListEntity>

    @Query("DELETE FROM WatchListEntity WHERE id == :movieId")
    suspend fun removeMovieFromWatchList(movieId: Int)
}
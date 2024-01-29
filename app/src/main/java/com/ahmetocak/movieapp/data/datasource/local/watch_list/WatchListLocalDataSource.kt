package com.ahmetocak.movieapp.data.datasource.local.watch_list

import com.ahmetocak.movieapp.model.watch_list.WatchListEntity

interface WatchListLocalDataSource {

    suspend fun addMovieToWatchList(watchListEntity: WatchListEntity)

    suspend fun getWatchList(): List<WatchListEntity>

    suspend fun removeMovieFromWatchList(movieId: Int)
}
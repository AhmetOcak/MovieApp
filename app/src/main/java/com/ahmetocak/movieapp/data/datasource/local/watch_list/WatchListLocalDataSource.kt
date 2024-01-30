package com.ahmetocak.movieapp.data.datasource.local.watch_list

import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.model.watch_list.WatchListEntity
import kotlinx.coroutines.flow.Flow

interface WatchListLocalDataSource {

    suspend fun addMovieToWatchList(watchListEntity: WatchListEntity): Response<Unit>

    suspend fun getWatchList(): Response<Flow<List<WatchListEntity>>>

    suspend fun removeMovieFromWatchList(movieId: Int): Response<Unit>
}
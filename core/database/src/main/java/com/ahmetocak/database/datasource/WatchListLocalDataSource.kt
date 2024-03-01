package com.ahmetocak.database.datasource

import com.ahmetocak.common.helpers.Response
import com.ahmetocak.database.entity.WatchListEntity
import kotlinx.coroutines.flow.Flow

interface WatchListLocalDataSource {

    suspend fun addMovieToWatchList(watchListEntity: WatchListEntity): Response<Unit>

    suspend fun getWatchList(): Response<Flow<List<WatchListEntity>>>

    suspend fun removeMovieFromWatchList(movieId: Int): Response<Unit>

    suspend fun deleteWatchList(): Response<Unit>
}
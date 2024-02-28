package com.ahmetocak.database.datasource

import com.ahmetocak.common.helpers.Response
import com.ahmetocak.database.dao.WatchListDao
import com.ahmetocak.database.entity.WatchListEntity
import com.ahmetocak.database.utils.dbCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WatchListLocalDataSourceImpl @Inject constructor(
    private val dao: WatchListDao
) : WatchListLocalDataSource {
    override suspend fun addMovieToWatchList(watchListEntity: WatchListEntity): Response<Unit> =
        dbCall { dao.addMovieToWatchList(watchListEntity) }

    override suspend fun getWatchList(): Response<Flow<List<WatchListEntity>>> =
        dbCall { dao.getWatchList() }

    override suspend fun removeMovieFromWatchList(movieId: Int) =
        dbCall { dao.removeMovieFromWatchList(movieId) }

    override suspend fun deleteWatchList(): Response<Unit> = dbCall { dao.deleteWatchList() }
}
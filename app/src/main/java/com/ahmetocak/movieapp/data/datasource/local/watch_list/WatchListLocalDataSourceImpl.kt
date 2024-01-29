package com.ahmetocak.movieapp.data.datasource.local.watch_list

import com.ahmetocak.movieapp.data.datasource.local.watch_list.db.WatchListDao
import com.ahmetocak.movieapp.model.watch_list.WatchListEntity
import javax.inject.Inject

class WatchListLocalDataSourceImpl @Inject constructor(
    private val dao: WatchListDao
) : WatchListLocalDataSource {
    override suspend fun addMovieToWatchList(watchListEntity: WatchListEntity) =
        dao.addMovieToWatchList(watchListEntity)

    override suspend fun getWatchList(): List<WatchListEntity> = dao.getWatchList()

    override suspend fun removeMovieFromWatchList(movieId: Int) =
        dao.removeMovieFromWatchList(movieId)
}
package com.ahmetocak.movieapp.domain.mapper

import com.ahmetocak.movieapp.model.firebase.firestore.WatchListMovie
import com.ahmetocak.movieapp.model.watch_list.WatchListEntity

fun WatchListMovie.toWatchListEntity(): WatchListEntity {
    return WatchListEntity(
        movieId = id ?: 0,
        movieName = name ?: "",
        releaseYear = releaseYear ?: "",
        voteAverage = voteAverage ?: 0f,
        voteCount = voteCount ?: 0,
        imageUrlPath = imageUrlPath ?: ""
    )
}

fun WatchListEntity.toWatchListMovie(): WatchListMovie {
    return WatchListMovie(
        id = movieId,
        name = movieName,
        releaseYear = releaseYear,
        voteAverage = voteAverage,
        voteCount = voteCount,
        imageUrlPath = imageUrlPath
    )
}
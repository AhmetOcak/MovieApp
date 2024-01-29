package com.ahmetocak.movieapp.domain.mapper

import com.ahmetocak.movieapp.model.firebase.firestore.WatchListMovie
import com.ahmetocak.movieapp.model.watch_list.WatchListEntity

fun WatchListMovie.toWatchListEntity(): WatchListEntity {
    return WatchListEntity(
        movieId = id ?: 0,
        movieName = name ?: "",
        releaseYear = releaseYear?.take(4) ?: "",
        genres = genres.joinToString(),
        voteAverage = voteAverage ?: 0f,
        voteCount = voteCount ?: 0,
        imageUrlPath = imageUrlPath ?: ""
    )
}
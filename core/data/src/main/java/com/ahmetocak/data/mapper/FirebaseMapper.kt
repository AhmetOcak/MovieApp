package com.ahmetocak.data.mapper

import com.ahmetocak.model.firebase.WatchListMovie
import com.ahmetocak.network.model.firebase.firestore.NetworkWatchListMovie

internal fun WatchListMovie.toNetworkWatchListMovie(): NetworkWatchListMovie {
    return NetworkWatchListMovie(
        id = id,
        name = name,
        releaseYear = releaseYear,
        voteAverage = voteAverage,
        voteCount = voteCount,
        imageUrlPath = imageUrlPath
    )
}

internal fun List<WatchListMovie>.toNetworkWatchListMovie(): List<NetworkWatchListMovie> {
    return this.map { it.toNetworkWatchListMovie() }
}
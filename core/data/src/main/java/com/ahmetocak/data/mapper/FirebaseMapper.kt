package com.ahmetocak.data.mapper

import com.ahmetocak.model.firebase.Auth
import com.ahmetocak.model.firebase.WatchListMovie
import com.ahmetocak.network.model.firebase.auth.NetworkAuth
import com.ahmetocak.network.model.firebase.firestore.NetworkWatchListMovie

internal fun Auth.toNetworkAuth(): NetworkAuth {
    return NetworkAuth(
        email = email,
        password = password
    )
}

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
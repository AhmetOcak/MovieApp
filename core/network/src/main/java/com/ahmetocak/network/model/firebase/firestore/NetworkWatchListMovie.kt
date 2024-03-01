package com.ahmetocak.network.model.firebase.firestore

data class NetworkWatchList(
    val watchList: List<NetworkWatchListMovie> = emptyList()
)

data class NetworkWatchListMovie(
    val id: Int? = null,
    val name: String? = null,
    val releaseYear: String? = null,
    val voteAverage: Float? = null,
    val voteCount: Int? = null,
    val imageUrlPath: String? = null
)
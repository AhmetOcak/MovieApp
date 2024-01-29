package com.ahmetocak.movieapp.model.firebase.firestore

import androidx.compose.runtime.Immutable

@Immutable
data class WatchListMovie(
    val id: Int? = null,
    val name: String? = null,
    val releaseYear: String? = null,
    val genres: List<String> = emptyList(),
    val voteAverage: Float? = null,
    val voteCount: Int? = null,
    val imageUrlPath: String? = null
)

@Immutable
data class WatchList(
    val watchList: List<WatchListMovie> = emptyList()
)
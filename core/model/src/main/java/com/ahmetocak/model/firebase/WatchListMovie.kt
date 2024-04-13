package com.ahmetocak.model.firebase

import androidx.compose.runtime.Immutable

data class WatchList(
    val watchList: List<WatchListMovie> = emptyList()
)

@Immutable
data class WatchListMovie(
    val id: Int? = null,
    val name: String? = null,
    val releaseYear: String? = null,
    val voteAverage: Float? = null,
    val voteCount: Int? = null,
    val imageUrlPath: String? = null
)
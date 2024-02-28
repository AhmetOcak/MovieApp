package com.ahmetocak.model.watch_list

import androidx.compose.runtime.Immutable

@Immutable
data class WatchList(
    val movieId: Int,
    val movieName: String,
    val releaseYear: String,
    val voteAverage: Float,
    val voteCount: Int,
    val imageUrlPath: String?
)
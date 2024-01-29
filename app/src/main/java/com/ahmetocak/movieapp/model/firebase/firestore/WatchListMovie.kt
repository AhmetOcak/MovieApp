package com.ahmetocak.movieapp.model.firebase.firestore

import androidx.compose.runtime.Immutable

@Immutable
data class WatchListMovie(
    val id: Int,
    val name: String,
    val releaseYear: String,
    val genres: List<String>,
    val voteAverage: Float,
    val voteCount: Int
)

package com.ahmetocak.model.movie

import androidx.compose.runtime.Immutable

@Immutable
data class RecommendedMovieContent(
    val id: Int,
    val movieName: String,
    val image: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int
)
package com.ahmetocak.model.movie

import androidx.compose.runtime.Immutable

@Immutable
data class Movie(
    val movies: List<MovieContent>,
    val totalPages: Int
)

@Immutable
data class MovieContent(
    val id: Int,
    val backdropImagePath: String?,
    val posterImagePath: String?,
    val releaseDate: String,
    val movieName: String,
    val voteAverage: Double,
    val voteCount: Int?
)
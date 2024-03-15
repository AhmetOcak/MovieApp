package com.ahmetocak.model.movie

import androidx.compose.runtime.Immutable

@Immutable
data class ActorMovies(
    val movies: List<ActorMoviesContent>
)

@Immutable
data class ActorMoviesContent(
    val id: Int,
    val movieName: String,
    val posterImagePath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int
)

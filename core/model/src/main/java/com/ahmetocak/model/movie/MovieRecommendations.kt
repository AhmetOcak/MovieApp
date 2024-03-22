package com.ahmetocak.model.movie

data class RecommendedMovieContent(
    val id: Int,
    val movieName: String,
    val image: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int
)
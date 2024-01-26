package com.ahmetocak.movieapp.model.movie

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("results")
    val movies: List<MovieContent>
)

data class MovieContent(
    val id: Int,

    @SerializedName("backdrop_path")
    val backdropImagePath: String?,

    @SerializedName("poster_path")
    val posterImagePath: String?,

    @SerializedName("genre_ids")
    val genreIds: List<Int>,

    @SerializedName("original_title")
    val movieName: String?,

    @SerializedName("vote_average")
    val voteAverage: Double?,

    @SerializedName("vote_count")
    val voteCount: Int?
)
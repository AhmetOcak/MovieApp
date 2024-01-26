package com.ahmetocak.movieapp.model.movie

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class Movie(
    @SerializedName("results")
    val movies: List<MovieContent>,

    @SerializedName("total_pages")
    val totalPages: Int
)

@Immutable
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
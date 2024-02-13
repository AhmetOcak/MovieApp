package com.ahmetocak.movieapp.model.movie_detail

import com.google.gson.annotations.SerializedName

data class MovieDetailDto(
    val id: Int,
    val genres: List<MovieGenreDto>,
    val overview: String?,

    @SerializedName("poster_path")
    val imageUrlPath: String?,

    @SerializedName("title")
    val movieName: String?,

    @SerializedName("vote_average")
    val voteAverage: Double?,

    @SerializedName("vote_count")
    val voteCount: Int?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("runtime")
    val duration: Int?,

    @SerializedName("original_title")
    val originalMovieName: String?
)

data class MovieGenreDto(
    val name: String
)
package com.ahmetocak.network.model.movie_detail

import com.google.gson.annotations.SerializedName

data class NetworkMovieDetail(
    val id: Int,
    val genres: List<NetworkMovieGenre>,
    val overview: String?,

    @SerializedName("poster_path")
    val posterImageUrlPath: String?,

    @SerializedName("backdrop_path")
    val backdropImageUrlPath: String?,

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

data class NetworkMovieGenre(
    val name: String
)
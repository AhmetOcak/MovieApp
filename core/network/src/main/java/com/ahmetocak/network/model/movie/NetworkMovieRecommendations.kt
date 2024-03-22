package com.ahmetocak.network.model.movie

import com.google.gson.annotations.SerializedName

data class NetworkMovieRecommendations(
    val page: Int,

    @SerializedName("results")
    val recommendations: List<NetworkRecommendedMovieContent>,

    @SerializedName("total_pages")
    val totalPages: Int
)

data class NetworkRecommendedMovieContent(
    val id: Int,

    @SerializedName("original_title")
    val movieName: String?,

    @SerializedName("poster_path")
    val image: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("vote_average")
    val voteAverage: Double?,

    @SerializedName("vote_count")
    val voteCount: Int?
)
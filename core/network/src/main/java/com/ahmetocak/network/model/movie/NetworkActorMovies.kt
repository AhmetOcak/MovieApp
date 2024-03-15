package com.ahmetocak.network.model.movie

import com.google.gson.annotations.SerializedName

data class NetworkActorMovies(
    @SerializedName("cast")
    val movies: List<NetworkActorMoviesContent>
)

data class NetworkActorMoviesContent(
    val id: Int,

    @SerializedName("title")
    val movieName: String? = null,

    @SerializedName("poster_path")
    val posterImagePath: String? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null,

    @SerializedName("vote_average")
    val voteAverage: Double? = null,

    @SerializedName("vote_count")
    val voteCount: Int? = null
)

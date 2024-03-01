package com.ahmetocak.network.model.movie_detail

import com.google.gson.annotations.SerializedName

data class NetworkMovieTrailer(
    @SerializedName("results")
    val trailers: List<NetworkTrailer>
)

data class NetworkTrailer(
    val name: String,
    val key: String
)
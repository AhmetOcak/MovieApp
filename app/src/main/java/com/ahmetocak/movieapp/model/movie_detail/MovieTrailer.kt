package com.ahmetocak.movieapp.model.movie_detail

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class MovieTrailer(
    @SerializedName("results")
    val trailers: List<Trailer>
)

@Immutable
data class Trailer(
    val name: String,
    val key: String
)
package com.ahmetocak.model.movie_detail

data class MovieTrailer(
    val trailers: List<Trailer>
)

data class Trailer(
    val name: String,
    val key: String
)
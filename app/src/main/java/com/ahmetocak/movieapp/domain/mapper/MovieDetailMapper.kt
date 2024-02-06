package com.ahmetocak.movieapp.domain.mapper

import com.ahmetocak.movieapp.domain.model.MovieDetail
import com.ahmetocak.movieapp.model.movie_detail.MovieDetailDto

fun MovieDetailDto.toMovieDetail(): MovieDetail {
    return MovieDetail(
        id = id,
        genres = genres.map { it.name },
        overview = overview ?: "",
        imageUrlPath = imageUrlPath ?: "",
        movieName = movieName ?: "",
        voteAverage = voteAverage?.toFloat() ?: 0f,
        voteCount = voteCount ?: 0,
        releaseDate = releaseDate ?: "",
        duration = duration?: 0,
        originalMovieName = originalMovieName ?: ""
    )
}
package com.ahmetocak.movieapp.domain.mapper

import com.ahmetocak.movieapp.domain.model.Cast
import com.ahmetocak.movieapp.domain.model.MovieCredit
import com.ahmetocak.movieapp.model.movie_detail.MovieCreditDto
import com.ahmetocak.movieapp.utils.TMDB

fun MovieCreditDto.toMovieCredit(): MovieCredit {
    return MovieCredit(
        cast = cast.map { castDto ->
            Cast(
                id = castDto.id,
                name = castDto.name ?: "",
                characterName = castDto.characterName ?: "",
                imageUrlPath = castDto.imageUrlPath ?: ""
            )
        },
        directorName = crew.firstOrNull { crewDto ->
            crewDto.job == TMDB.JOB_DIRECTOR_KEY
        }?.name ?: ""
    )
}
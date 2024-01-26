package com.ahmetocak.movieapp.data.repository.movie

import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.model.movie.Movie

interface MovieRepository {

    suspend fun getNowPlayingMoviesFirstPage(): Response<Movie>
}
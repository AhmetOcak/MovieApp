package com.ahmetocak.movieapp.data.datasource.remote.movie

import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.model.movie.Movie

interface MovieRemoteDataSource {

    suspend fun getNowPlayingMoviesFirstPage(): Response<Movie>

    suspend fun getPopularMoviesFirstPage(): Response<Movie>
}
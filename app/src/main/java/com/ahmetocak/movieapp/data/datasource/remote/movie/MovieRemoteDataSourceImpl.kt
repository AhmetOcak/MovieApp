package com.ahmetocak.movieapp.data.datasource.remote.movie

import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.common.helpers.apiCall
import com.ahmetocak.movieapp.data.datasource.remote.movie.api.MovieApi
import com.ahmetocak.movieapp.model.movie.Movie
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val api: MovieApi
) : MovieRemoteDataSource {
    override suspend fun getNowPlayingMoviesFirstPage(): Response<Movie> =
        apiCall { api.getNowPlayingMovies() }
}
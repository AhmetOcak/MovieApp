package com.ahmetocak.movieapp.data.datasource.remote.movie

import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.common.helpers.apiCall
import com.ahmetocak.movieapp.data.datasource.remote.movie.api.MovieApi
import com.ahmetocak.movieapp.model.movie.Movie
import com.ahmetocak.movieapp.model.movie_detail.MovieCreditDto
import com.ahmetocak.movieapp.model.movie_detail.MovieDetailDto
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val api: MovieApi
) : MovieRemoteDataSource {
    override suspend fun getNowPlayingMoviesFirstPage(): Response<Movie> =
        apiCall { api.getNowPlayingMovies() }

    override suspend fun getPopularMoviesFirstPage(): Response<Movie> =
        apiCall { api.getPopularMovies() }

    override suspend fun getMovieDetails(movieId: Int): Response<MovieDetailDto> =
        apiCall { api.getMovieDetails(movieId) }

    override suspend fun getMovieCredits(movieId: Int): Response<MovieCreditDto> =
        apiCall { api.getMovieCredits(movieId) }
}
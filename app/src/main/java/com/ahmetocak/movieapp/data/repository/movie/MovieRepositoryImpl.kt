package com.ahmetocak.movieapp.data.repository.movie

import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.data.datasource.remote.movie.MovieRemoteDataSource
import com.ahmetocak.movieapp.model.movie.Movie
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource
) : MovieRepository {
    override suspend fun getNowPlayingMoviesFirstPage(): Response<Movie> =
        movieRemoteDataSource.getNowPlayingMoviesFirstPage()

    override suspend fun getPopularMoviesFirstPage(): Response<Movie> =
        movieRemoteDataSource.getPopularMoviesFirstPage()
}
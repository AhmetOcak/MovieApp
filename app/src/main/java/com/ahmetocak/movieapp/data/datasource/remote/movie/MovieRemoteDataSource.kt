package com.ahmetocak.movieapp.data.datasource.remote.movie

import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.model.movie.Movie
import com.ahmetocak.movieapp.model.movie_detail.MovieCreditDto
import com.ahmetocak.movieapp.model.movie_detail.MovieDetailDto
import com.ahmetocak.movieapp.model.movie_detail.MovieTrailer

interface MovieRemoteDataSource {

    suspend fun getNowPlayingMoviesFirstPage(): Response<Movie>

    suspend fun getPopularMoviesFirstPage(): Response<Movie>

    suspend fun getMovieDetails(movieId: Int): Response<MovieDetailDto>

    suspend fun getMovieCredits(movieId: Int): Response<MovieCreditDto>

    suspend fun getMovieTrailers(movieId: Int): Response<MovieTrailer>
}
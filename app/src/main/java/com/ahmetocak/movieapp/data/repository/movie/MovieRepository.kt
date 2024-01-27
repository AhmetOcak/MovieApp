package com.ahmetocak.movieapp.data.repository.movie

import androidx.paging.PagingData
import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.domain.model.MovieDetail
import com.ahmetocak.movieapp.model.movie.Movie
import com.ahmetocak.movieapp.model.movie.MovieContent
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getNowPlayingMoviesFirstPage(): Response<Movie>

    suspend fun getPopularMoviesFirstPage(): Response<Movie>

    fun getAllNowPlayingMovies(): Flow<PagingData<MovieContent>>

    fun getAllPopularMovies(): Flow<PagingData<MovieContent>>

    suspend fun getMovieDetails(movieId: Int): Response<MovieDetail>
}
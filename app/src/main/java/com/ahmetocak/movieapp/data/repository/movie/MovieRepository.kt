package com.ahmetocak.movieapp.data.repository.movie

import androidx.paging.PagingData
import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.domain.model.MovieCredit
import com.ahmetocak.movieapp.domain.model.MovieDetail
import com.ahmetocak.movieapp.model.movie.Movie
import com.ahmetocak.movieapp.model.movie.MovieContent
import com.ahmetocak.movieapp.model.movie_detail.MovieTrailer
import com.ahmetocak.movieapp.model.watch_list.WatchListEntity
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getNowPlayingMoviesFirstPage(): Response<Movie>

    suspend fun getPopularMoviesFirstPage(): Response<Movie>

    fun getAllNowPlayingMovies(): Flow<PagingData<MovieContent>>

    fun getAllPopularMovies(): Flow<PagingData<MovieContent>>

    suspend fun getMovieDetails(movieId: Int): Response<MovieDetail>

    suspend fun getMovieCredits(movieId: Int): Response<MovieCredit>

    suspend fun getMovieTrailers(movieId: Int): Response<MovieTrailer>

    fun searchMovie(query: String): Flow<PagingData<MovieContent>>

    suspend fun addMovieToWatchList(watchListEntity: WatchListEntity): Response<Unit>

    suspend fun getWatchList(): Response<List<WatchListEntity>>

    suspend fun removeMovieFromWatchList(movieId: Int): Response<Unit>
}
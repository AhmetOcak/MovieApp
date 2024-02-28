package com.ahmetocak.data.repository.movie

import androidx.paging.PagingData
import com.ahmetocak.common.helpers.Response
import com.ahmetocak.model.firebase.WatchListMovie
import com.ahmetocak.model.movie.Movie
import com.ahmetocak.model.movie.MovieContent
import com.ahmetocak.model.movie_detail.MovieCredit
import com.ahmetocak.model.movie_detail.MovieDetail
import com.ahmetocak.model.movie_detail.MovieTrailer
import com.ahmetocak.model.watch_list.WatchList
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

    suspend fun addMovieToWatchList(watchListMovie: WatchListMovie): Response<Unit>

    suspend fun getWatchList(): Response<Flow<List<WatchList>>>

    suspend fun removeMovieFromWatchList(movieId: Int): Response<Unit>

    suspend fun deleteWatchList(): Response<Unit>
}
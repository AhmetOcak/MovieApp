package com.ahmetocak.data.repository.movie

import androidx.paging.PagingData
import com.ahmetocak.common.helpers.Response
import com.ahmetocak.model.firebase.WatchListMovie
import com.ahmetocak.model.movie.ActorDetails
import com.ahmetocak.model.movie.ActorMovies
import com.ahmetocak.model.movie.Movie
import com.ahmetocak.model.movie.MovieContent
import com.ahmetocak.model.movie.RecommendedMovieContent
import com.ahmetocak.model.movie.UserReviewResults
import com.ahmetocak.model.movie_detail.MovieCredit
import com.ahmetocak.model.movie_detail.MovieDetail
import com.ahmetocak.model.movie_detail.MovieTrailer
import com.ahmetocak.model.watch_list.WatchList
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getTrendingMoviesFirstPage(): Response<Movie>

    suspend fun getTopRatedMoviesFirstPage(): Response<Movie>

    suspend fun getUpcomingMoviesFirstPage(): Response<Movie>

    fun getAllTrendingMovies(): Flow<PagingData<MovieContent>>

    fun getAllTopRatedMovies(): Flow<PagingData<MovieContent>>

    fun getAllUpcomingMovies(): Flow<PagingData<MovieContent>>

    suspend fun getMovieDetails(movieId: Int): Response<MovieDetail>

    suspend fun getMovieCredits(movieId: Int): Response<MovieCredit>

    suspend fun getMovieTrailers(movieId: Int): Response<MovieTrailer>

    fun searchMovie(query: String): Flow<PagingData<MovieContent>>

    suspend fun addMovieToWatchList(watchListMovie: WatchListMovie): Response<Unit>

    suspend fun getWatchList(): Response<Flow<List<WatchList>>>

    suspend fun removeMovieFromWatchList(movieId: Int): Response<Unit>

    suspend fun deleteWatchList(): Response<Unit>

    suspend fun getActorDetails(actorId: Int): Response<ActorDetails>

    suspend fun getActorMovies(actorId: Int): Response<ActorMovies>

    fun getUserMovieReviews(movieId: Int): Flow<PagingData<UserReviewResults>>

    fun getMovieRecommendations(movieId: Int): Flow<PagingData<RecommendedMovieContent>>
}
package com.ahmetocak.network.api

import com.ahmetocak.network.BuildConfig
import com.ahmetocak.network.model.movie.NetworkActorDetails
import com.ahmetocak.network.model.movie.NetworkActorMovies
import com.ahmetocak.network.helpers.NetworkConstants
import com.ahmetocak.network.model.movie.NetworkMovie
import com.ahmetocak.network.model.movie.NetworkMovieRecommendations
import com.ahmetocak.network.model.movie.NetworkUserMovieReviews
import com.ahmetocak.network.model.movie_detail.NetworkMovieCredit
import com.ahmetocak.network.model.movie_detail.NetworkMovieDetail
import com.ahmetocak.network.model.movie_detail.NetworkMovieTrailer
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

interface MovieApi {

    @GET(NetworkConstants.EndPoints.NOW_PLAYING)
    suspend fun getNowPlayingMovies(
        @Query(NetworkConstants.Queries.PAGE) page: Int = 1,
        @Query(NetworkConstants.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(NetworkConstants.Queries.LANGUAGE) language: String = Locale.getDefault().toLanguageTag()
    ): NetworkMovie

    @GET(NetworkConstants.EndPoints.POPULAR)
    suspend fun getPopularMovies(
        @Query(NetworkConstants.Queries.PAGE) page: Int = 1,
        @Query(NetworkConstants.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(NetworkConstants.Queries.LANGUAGE) language: String = Locale.getDefault().toLanguageTag()
    ): NetworkMovie

    @GET(NetworkConstants.EndPoints.MOVIE_DETAILS)
    suspend fun getMovieDetails(
        @Path(NetworkConstants.Paths.MOVIE_ID) movieId: Int,
        @Query(NetworkConstants.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(NetworkConstants.Queries.LANGUAGE) language: String = Locale.getDefault().toLanguageTag()
    ): NetworkMovieDetail

    @GET(NetworkConstants.EndPoints.MOVIE_CREDITS)
    suspend fun getMovieCredits(
        @Path(NetworkConstants.Paths.MOVIE_ID) movieId: Int,
        @Query(NetworkConstants.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(NetworkConstants.Queries.LANGUAGE) language: String = Locale.getDefault().toLanguageTag()
    ): NetworkMovieCredit

    @GET(NetworkConstants.EndPoints.MOVIE_TRAILERS)
    suspend fun getMovieTrailers(
        @Path(NetworkConstants.Paths.MOVIE_ID) movieId: Int,
        @Query(NetworkConstants.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(NetworkConstants.Queries.LANGUAGE) language: String = Locale.getDefault().toLanguageTag()
    ): NetworkMovieTrailer

    @GET(NetworkConstants.EndPoints.SEARCH_MOVIE)
    suspend fun searchMovie(
        @Query(NetworkConstants.Queries.PAGE) page: Int = 1,
        @Query(NetworkConstants.Queries.SEARCH_QUERY) query: String,
        @Query(NetworkConstants.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(NetworkConstants.Queries.LANGUAGE) language: String = Locale.getDefault().toLanguageTag()
    ): NetworkMovie

    @GET(NetworkConstants.EndPoints.ACTOR_DETAILS)
    suspend fun getActorDetails(
        @Path(NetworkConstants.Paths.ACTOR_ID) actorId: Int,
        @Query(NetworkConstants.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(NetworkConstants.Queries.LANGUAGE) language: String = Locale.getDefault().toLanguageTag()
    ): NetworkActorDetails

    @GET(NetworkConstants.EndPoints.ACTOR_MOVIES)
    suspend fun getActorMovies(
        @Path(NetworkConstants.Paths.ACTOR_ID) actorId: Int,
        @Query(NetworkConstants.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(NetworkConstants.Queries.LANGUAGE) language: String = Locale.getDefault().toLanguageTag()
    ): NetworkActorMovies

    @GET(NetworkConstants.EndPoints.USER_REVIEWS)
    suspend fun getUserMovieReviews(
        @Path(NetworkConstants.Paths.MOVIE_ID) movieId: Int,
        @Query(NetworkConstants.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(NetworkConstants.Queries.PAGE) page: Int = 1
    ): NetworkUserMovieReviews

    @GET(NetworkConstants.EndPoints.RECOMMENDATIONS)
    suspend fun getMovieRecommendations(
        @Path(NetworkConstants.Paths.MOVIE_ID) movieId: Int,
        @Query(NetworkConstants.Queries.PAGE) page: Int = 1,
        @Query(NetworkConstants.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(NetworkConstants.Queries.LANGUAGE) language: String = Locale.getDefault().toLanguageTag()
    ): NetworkMovieRecommendations
}
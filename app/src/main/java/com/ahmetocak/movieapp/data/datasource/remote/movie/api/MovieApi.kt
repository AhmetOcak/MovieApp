package com.ahmetocak.movieapp.data.datasource.remote.movie.api

import com.ahmetocak.movieapp.BuildConfig
import com.ahmetocak.movieapp.model.movie.Movie
import com.ahmetocak.movieapp.model.movie_detail.MovieCreditDto
import com.ahmetocak.movieapp.model.movie_detail.MovieDetailDto
import com.ahmetocak.movieapp.model.movie_detail.MovieTrailer
import com.ahmetocak.movieapp.utils.Network
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

interface MovieApi {

    @GET(Network.EndPoints.NOW_PLAYING)
    suspend fun getNowPlayingMovies(
        @Query(Network.Queries.PAGE) page: Int = 1,
        @Query(Network.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(Network.Queries.LANGUAGE) language: String = Locale.getDefault().toLanguageTag()
    ): Movie

    @GET(Network.EndPoints.POPULAR)
    suspend fun getPopularMovies(
        @Query(Network.Queries.PAGE) page: Int = 1,
        @Query(Network.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(Network.Queries.LANGUAGE) language: String = Locale.getDefault().toLanguageTag()
    ): Movie

    @GET(Network.EndPoints.MOVIE_DETAILS)
    suspend fun getMovieDetails(
        @Path(Network.Paths.MOVIE_ID) movieId: Int,
        @Query(Network.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(Network.Queries.LANGUAGE) language: String = Locale.getDefault().toLanguageTag()
    ): MovieDetailDto

    @GET(Network.EndPoints.MOVIE_CREDITS)
    suspend fun getMovieCredits(
        @Path(Network.Paths.MOVIE_ID) movieId: Int,
        @Query(Network.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(Network.Queries.LANGUAGE) language: String = Locale.getDefault().toLanguageTag()
    ): MovieCreditDto

    @GET(Network.EndPoints.MOVIE_TRAILERS)
    suspend fun getMovieTrailers(
        @Path(Network.Paths.MOVIE_ID) movieId: Int,
        @Query(Network.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(Network.Queries.LANGUAGE) language: String = Locale.getDefault().toLanguageTag()
    ): MovieTrailer

    @GET(Network.EndPoints.SEARCH_MOVIE)
    suspend fun searchMovie(
        @Query(Network.Queries.PAGE) page: Int = 1,
        @Query(Network.Queries.SEARCH_QUERY) query: String,
        @Query(Network.Queries.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(Network.Queries.LANGUAGE) language: String = Locale.getDefault().toLanguageTag()
    ): Movie
}
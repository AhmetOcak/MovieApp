package com.ahmetocak.movieapp.data.datasource.remote.movie.api

import com.ahmetocak.movieapp.model.movie.Movie
import com.ahmetocak.movieapp.model.movie_detail.MovieCreditDto
import com.ahmetocak.movieapp.model.movie_detail.MovieDetailDto
import com.ahmetocak.movieapp.model.movie_detail.MovieTrailer
import com.ahmetocak.movieapp.utils.Network
import com.google.android.gms.common.api.internal.ApiKey
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET(Network.EndPoints.NOW_PLAYING)
    suspend fun getNowPlayingMovies(
        @Query(Network.Queries.PAGE) page: Int = 1,
        @Query(Network.Queries.API_KEY) apiKey: String = "ce10126f10a15837182856698036de55"
    ): Movie

    @GET(Network.EndPoints.POPULAR)
    suspend fun getPopularMovies(
        @Query(Network.Queries.PAGE) page: Int = 1,
        @Query(Network.Queries.API_KEY) apiKey: String = "ce10126f10a15837182856698036de55"
    ): Movie

    @GET(Network.EndPoints.MOVIE_DETAILS)
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query(Network.Queries.API_KEY) apiKey: String = "ce10126f10a15837182856698036de55"
    ): MovieDetailDto

    @GET(Network.EndPoints.MOVIE_CREDITS)
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query(Network.Queries.API_KEY) apiKey: String = "ce10126f10a15837182856698036de55"
    ): MovieCreditDto

    @GET(Network.EndPoints.MOVIE_TRAILERS)
    suspend fun getMovieTrailers(
        @Path("movie_id") movieId: Int,
        @Query(Network.Queries.API_KEY) apiKey: String = "ce10126f10a15837182856698036de55"
    ): MovieTrailer
}
package com.ahmetocak.movieapp.data.datasource.remote.movie.api

import com.ahmetocak.movieapp.model.movie.Movie
import com.ahmetocak.movieapp.utils.Network
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET(Network.EndPoints.NOW_PLAYING)
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = "ce10126f10a15837182856698036de55"
    ): Movie
}
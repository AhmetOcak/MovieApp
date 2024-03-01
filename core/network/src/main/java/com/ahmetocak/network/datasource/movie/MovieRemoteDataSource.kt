package com.ahmetocak.network.datasource.movie

import com.ahmetocak.common.helpers.Response
import com.ahmetocak.network.model.movie.NetworkMovie
import com.ahmetocak.network.model.movie_detail.NetworkMovieCredit
import com.ahmetocak.network.model.movie_detail.NetworkMovieDetail
import com.ahmetocak.network.model.movie_detail.NetworkMovieTrailer

interface MovieRemoteDataSource {

    suspend fun getNowPlayingMoviesFirstPage(): Response<NetworkMovie>

    suspend fun getPopularMoviesFirstPage(): Response<NetworkMovie>

    suspend fun getMovieDetails(movieId: Int): Response<NetworkMovieDetail>

    suspend fun getMovieCredits(movieId: Int): Response<NetworkMovieCredit>

    suspend fun getMovieTrailers(movieId: Int): Response<NetworkMovieTrailer>
}
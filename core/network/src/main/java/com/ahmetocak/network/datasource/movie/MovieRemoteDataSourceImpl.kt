package com.ahmetocak.network.datasource.movie

import com.ahmetocak.common.helpers.Response
import com.ahmetocak.network.api.MovieApi
import com.ahmetocak.network.utils.apiCall
import com.ahmetocak.network.model.movie.NetworkMovie
import com.ahmetocak.network.model.movie_detail.NetworkMovieCredit
import com.ahmetocak.network.model.movie_detail.NetworkMovieDetail
import com.ahmetocak.network.model.movie_detail.NetworkMovieTrailer
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val api: MovieApi
) : MovieRemoteDataSource {
    override suspend fun getNowPlayingMoviesFirstPage(): Response<NetworkMovie> =
        apiCall { api.getNowPlayingMovies() }

    override suspend fun getPopularMoviesFirstPage(): Response<NetworkMovie> =
        apiCall { api.getPopularMovies() }

    override suspend fun getMovieDetails(movieId: Int): Response<NetworkMovieDetail> =
        apiCall { api.getMovieDetails(movieId) }

    override suspend fun getMovieCredits(movieId: Int): Response<NetworkMovieCredit> =
        apiCall { api.getMovieCredits(movieId) }

    override suspend fun getMovieTrailers(movieId: Int): Response<NetworkMovieTrailer> =
        apiCall { api.getMovieTrailers(movieId) }
}
package com.ahmetocak.movieapp.data.repository.movie

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.common.mapResponse
import com.ahmetocak.movieapp.data.datasource.remote.movie.MovieRemoteDataSource
import com.ahmetocak.movieapp.data.datasource.remote.movie.api.MovieApi
import com.ahmetocak.movieapp.data.datasource.remote.movie.paging_source.MoviesPagingSource
import com.ahmetocak.movieapp.domain.mapper.toMovieCast
import com.ahmetocak.movieapp.domain.mapper.toMovieDetail
import com.ahmetocak.movieapp.domain.model.MovieCredit
import com.ahmetocak.movieapp.domain.model.MovieDetail
import com.ahmetocak.movieapp.model.movie.Movie
import com.ahmetocak.movieapp.model.movie.MovieContent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val api: MovieApi
) : MovieRepository {
    override suspend fun getNowPlayingMoviesFirstPage(): Response<Movie> =
        movieRemoteDataSource.getNowPlayingMoviesFirstPage()

    override suspend fun getPopularMoviesFirstPage(): Response<Movie> =
        movieRemoteDataSource.getPopularMoviesFirstPage()

    override fun getAllNowPlayingMovies(): Flow<PagingData<MovieContent>> {
        val moviesPagingSource = MoviesPagingSource(
            apiCall = { currentPageNumber ->
                api.getNowPlayingMovies(page = currentPageNumber)
            }
        )
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { moviesPagingSource }
        ).flow
    }

    override fun getAllPopularMovies(): Flow<PagingData<MovieContent>> {
        val moviesPagingSource = MoviesPagingSource(
            apiCall = { currentPageNumber ->
                api.getPopularMovies(page = currentPageNumber)
            }
        )
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { moviesPagingSource }
        ).flow
    }

    override suspend fun getMovieDetails(movieId: Int): Response<MovieDetail> =
        movieRemoteDataSource.getMovieDetails(movieId).mapResponse { it.toMovieDetail() }

    override suspend fun getMovieCredits(movieId: Int): Response<MovieCredit> =
        movieRemoteDataSource.getMovieCredits(movieId).mapResponse { it.toMovieCast() }
}
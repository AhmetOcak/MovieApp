package com.ahmetocak.data.repository.movie

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ahmetocak.common.helpers.Response
import com.ahmetocak.common.utils.mapResponse
import com.ahmetocak.data.mapper.toActorDetails
import com.ahmetocak.data.mapper.toActorMovies
import com.ahmetocak.data.mapper.toMovie
import com.ahmetocak.data.mapper.toMovieContent
import com.ahmetocak.data.mapper.toMovieCredit
import com.ahmetocak.data.mapper.toMovieDetail
import com.ahmetocak.data.mapper.toMovieTrailer
import com.ahmetocak.data.mapper.toWatchListEntity
import com.ahmetocak.data.mapper.toWatchList
import com.ahmetocak.database.datasource.WatchListLocalDataSource
import com.ahmetocak.model.firebase.WatchListMovie
import com.ahmetocak.model.movie.ActorDetails
import com.ahmetocak.model.movie.ActorMovies
import com.ahmetocak.model.movie.Movie
import com.ahmetocak.model.movie.MovieContent
import com.ahmetocak.model.movie_detail.MovieCredit
import com.ahmetocak.model.movie_detail.MovieDetail
import com.ahmetocak.model.movie_detail.MovieTrailer
import com.ahmetocak.model.watch_list.WatchList
import com.ahmetocak.network.api.MovieApi
import com.ahmetocak.network.datasource.movie.MovieRemoteDataSource
import com.ahmetocak.network.datasource.movie.paging_source.MoviesPagingSource
import com.ahmetocak.network.datasource.movie.paging_source.SearchMoviesPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val api: MovieApi,
    private val watchListLocalDataSource: WatchListLocalDataSource
) : MovieRepository {

    override suspend fun getNowPlayingMoviesFirstPage(): Response<Movie> =
        movieRemoteDataSource.getNowPlayingMoviesFirstPage().mapResponse { it.toMovie() }

    override suspend fun getPopularMoviesFirstPage(): Response<Movie> =
        movieRemoteDataSource.getPopularMoviesFirstPage().mapResponse { it.toMovie() }

    override fun getAllNowPlayingMovies(): Flow<PagingData<MovieContent>> {
        val moviesPagingSource = MoviesPagingSource(
            apiCall = { currentPageNumber ->
                api.getNowPlayingMovies(page = currentPageNumber)
            }
        )
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { moviesPagingSource }
        ).flow.map {
            it.map { networkMovieContent ->
                networkMovieContent.toMovieContent()
            }
        }
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
        ).flow.map {
            it.map { networkMovieContent ->
                networkMovieContent.toMovieContent()
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Response<MovieDetail> =
        movieRemoteDataSource.getMovieDetails(movieId).mapResponse { it.toMovieDetail() }

    override suspend fun getMovieCredits(movieId: Int): Response<MovieCredit> =
        movieRemoteDataSource.getMovieCredits(movieId).mapResponse { it.toMovieCredit() }

    override suspend fun getMovieTrailers(movieId: Int): Response<MovieTrailer> =
        movieRemoteDataSource.getMovieTrailers(movieId).mapResponse { it.toMovieTrailer() }

    override fun searchMovie(query: String): Flow<PagingData<MovieContent>> {
        val searchMoviePagingSource = SearchMoviesPagingSource(
            query = query,
            api = api
        )
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { searchMoviePagingSource }
        ).flow.map {
            it.map { networkMovieContent ->
                networkMovieContent.toMovieContent()
            }
        }
    }

    override suspend fun addMovieToWatchList(watchListMovie: WatchListMovie) =
        watchListLocalDataSource.addMovieToWatchList(watchListMovie.toWatchListEntity())

    override suspend fun getWatchList(): Response<Flow<List<WatchList>>> =
        watchListLocalDataSource.getWatchList().mapResponse { it.toWatchList() }

    override suspend fun removeMovieFromWatchList(movieId: Int) =
        watchListLocalDataSource.removeMovieFromWatchList(movieId)

    override suspend fun deleteWatchList(): Response<Unit> =
        watchListLocalDataSource.deleteWatchList()

    override suspend fun getActorDetails(actorId: Int): Response<ActorDetails> =
        movieRemoteDataSource.getActorDetails(actorId).mapResponse { it.toActorDetails() }

    override suspend fun getActorMovies(actorId: Int): Response<ActorMovies> =
        movieRemoteDataSource.getActorMovies(actorId).mapResponse { it.toActorMovies() }
}
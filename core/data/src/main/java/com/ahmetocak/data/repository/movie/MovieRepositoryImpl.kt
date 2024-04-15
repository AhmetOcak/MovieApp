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
import com.ahmetocak.data.mapper.toRecommendedMovieContent
import com.ahmetocak.data.mapper.toUserReviewResults
import com.ahmetocak.data.mapper.toWatchListEntity
import com.ahmetocak.data.mapper.toWatchList
import com.ahmetocak.database.datasource.WatchListLocalDataSource
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
import com.ahmetocak.network.api.MovieApi
import com.ahmetocak.network.datasource.movie.MovieRemoteDataSource
import com.ahmetocak.network.datasource.movie.paging_source.MoviesPagingSource
import com.ahmetocak.network.datasource.movie.paging_source.RecommendedMoviesPagingSource
import com.ahmetocak.network.datasource.movie.paging_source.SearchMoviesPagingSource
import com.ahmetocak.network.datasource.movie.paging_source.UserMovieReviewsPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val api: MovieApi,
    private val watchListLocalDataSource: WatchListLocalDataSource
) : MovieRepository {

    override suspend fun getTrendingMoviesFirstPage(): Response<Movie> =
        movieRemoteDataSource.getTrendingMoviesFirstPage().mapResponse { it.toMovie() }

    override suspend fun getTopRatedMoviesFirstPage(): Response<Movie> =
        movieRemoteDataSource.getTopRatedMoviesFirstPage().mapResponse { it.toMovie() }

    override suspend fun getUpcomingMoviesFirstPage(): Response<Movie> =
        movieRemoteDataSource.getUpcomingMoviesFirstPage().mapResponse { it.toMovie() }

    override fun getAllTrendingMovies(): Flow<PagingData<MovieContent>> {
        val moviesPagingSource = MoviesPagingSource(
            apiCall = { currentPageNumber ->
                api.getTrendingMovies(page = currentPageNumber)
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

    override fun getAllTopRatedMovies(): Flow<PagingData<MovieContent>> {
        val moviesPagingSource = MoviesPagingSource(
            apiCall = { currentPageNumber ->
                api.getTopRatedMovies(page = currentPageNumber)
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

    override fun getAllUpcomingMovies(): Flow<PagingData<MovieContent>> {
        val moviesPagingSource = MoviesPagingSource(
            apiCall = { currentPageNumber ->
                api.getUpcomingMovies(page = currentPageNumber)
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

    override suspend fun observeWatchList(): Response<Flow<List<WatchList>>> =
        watchListLocalDataSource.observeWatchList().mapResponse { it.toWatchList() }

    override suspend fun removeMovieFromWatchList(movieId: Int) =
        watchListLocalDataSource.removeMovieFromWatchList(movieId)

    override suspend fun deleteWatchList(): Response<Unit> =
        watchListLocalDataSource.deleteWatchList()

    override suspend fun getActorDetails(actorId: Int): Response<ActorDetails> =
        movieRemoteDataSource.getActorDetails(actorId).mapResponse { it.toActorDetails() }

    override suspend fun getActorMovies(actorId: Int): Response<ActorMovies> =
        movieRemoteDataSource.getActorMovies(actorId).mapResponse { it.toActorMovies() }

    override fun getUserMovieReviews(movieId: Int): Flow<PagingData<UserReviewResults>> {
        val userReviewsPagingSource = UserMovieReviewsPagingSource(
            movieId = movieId,
            api = api
        )

        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { userReviewsPagingSource }
        ).flow.map {
            it.map {  networkUserReviewResults ->
                networkUserReviewResults.toUserReviewResults()
            }
        }
    }

    override fun getMovieRecommendations(movieId: Int): Flow<PagingData<RecommendedMovieContent>> {
        val recommendedMoviesPagingSource = RecommendedMoviesPagingSource(
            api = api,
            movieId = movieId
        )
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { recommendedMoviesPagingSource }
        ).flow.map {
            it.map { networkMovieContent ->
                networkMovieContent.toRecommendedMovieContent()
            }
        }
    }
}
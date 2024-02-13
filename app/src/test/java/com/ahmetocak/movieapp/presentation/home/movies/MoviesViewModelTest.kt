package com.ahmetocak.movieapp.presentation.home.movies

import app.cash.turbine.test
import com.ahmetocak.movieapp.data.datasource.local.watch_list.WatchListLocalDataSource
import com.ahmetocak.movieapp.data.datasource.remote.movie.FakeMovieRemoteDataSourceImpl
import com.ahmetocak.movieapp.data.datasource.remote.movie.api.MovieApi
import com.ahmetocak.movieapp.data.repository.movie.MovieRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MoviesViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var api: MovieApi

    @Mock
    private lateinit var watchListLocalDataSource: WatchListLocalDataSource

    private lateinit var viewModel: MoviesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        viewModel = MoviesViewModel(
            MovieRepositoryImpl(
                FakeMovieRemoteDataSourceImpl(),
                api,
                watchListLocalDataSource
            ),
            testDispatcher
        )
    }

    @Test
    fun getMovies_returnExpectedData() {
        runTest {
            viewModel.uiState.test {
                assertEquals(
                    MoviesUiState(
                        nowPlayingMoviesState = MovieState.OnDataLoaded(
                            movieList = FakeMovieRemoteDataSourceImpl.expectedMovieListData.movies
                        ),
                        popularMoviesState = MovieState.OnDataLoaded(
                            movieList = FakeMovieRemoteDataSourceImpl.expectedMovieListData.movies
                        )
                    ),
                    awaitItem()
                )
            }
        }
    }
}
package com.ahmetocak.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.helpers.Response
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.domain.movie.GetTopRatedMoviesFirstPageUseCase
import com.ahmetocak.domain.movie.GetTrendingMoviesFirstPage
import com.ahmetocak.domain.movie.GetUpcomingMoviesFirstPageUseCase
import com.ahmetocak.model.movie.MovieContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val getTopRatedMoviesFirstPageUseCase: GetTopRatedMoviesFirstPageUseCase,
    private val getUpcomingMoviesFirstPageUseCase: GetUpcomingMoviesFirstPageUseCase,
    private val getTrendingMoviesFirstPage: GetTrendingMoviesFirstPage
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    init {
        getTrendingMovies()
        getTopRatedMovies()
        getUpcomingMovies()
    }

    private fun getTrendingMovies() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = getTrendingMoviesFirstPage()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(trendingMoviesState = MovieState.OnDataLoaded(response.data.movies))
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(trendingMoviesState = MovieState.OnError(response.errorMessage))
                    }
                }
            }
        }
    }

    private fun getTopRatedMovies() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = getTopRatedMoviesFirstPageUseCase()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(topRatedMoviesState = MovieState.OnDataLoaded(response.data.movies))
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(topRatedMoviesState = MovieState.OnError(response.errorMessage))
                    }
                }
            }
        }
    }

    private fun getUpcomingMovies() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = getUpcomingMoviesFirstPageUseCase()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(upcomingMoviesState = MovieState.OnDataLoaded(response.data.movies))
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(upcomingMoviesState = MovieState.OnError(response.errorMessage))
                    }
                }
            }
        }
    }
}

data class MoviesUiState(
    val trendingMoviesState: MovieState = MovieState.Loading,
    val topRatedMoviesState: MovieState = MovieState.Loading,
    val upcomingMoviesState: MovieState = MovieState.Loading
)

sealed class MovieState {
    data object Loading : MovieState()
    data class OnDataLoaded(val movieList: List<MovieContent>) : MovieState()
    data class OnError(val errorMessage: UiText) : MovieState()
}
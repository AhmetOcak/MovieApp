package com.ahmetocak.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.helpers.Response
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.domain.movie.GetTopRatedMoviesFirstPageUseCase
import com.ahmetocak.domain.movie.GetTrendingMoviesFirstPageUseCase
import com.ahmetocak.domain.movie.GetUpcomingMoviesFirstPageUseCase
import com.ahmetocak.model.movie.MovieContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
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
    private val getTrendingMoviesFirstPageUseCase: GetTrendingMoviesFirstPageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    init {
        getAllDataAndUpdateUi()
    }

    private fun getAllDataAndUpdateUi() {
        viewModelScope.launch(ioDispatcher) {
            val trendingMoviesDeferred = async { getTrendingMoviesFirstPageUseCase() }
            val topRatedMoviesDeferred = async { getTopRatedMoviesFirstPageUseCase() }
            val upcomingMoviesDeferred = async { getUpcomingMoviesFirstPageUseCase() }

            val trendingMoviesResponse = trendingMoviesDeferred.await()
            val topRatedMoviesResponse = topRatedMoviesDeferred.await()
            val upcomingMoviesResponse = upcomingMoviesDeferred.await()

            if (trendingMoviesResponse is Response.Success &&
                topRatedMoviesResponse is Response.Success &&
                upcomingMoviesResponse is Response.Success
            ) {
                _uiState.update {
                    it.copy(
                        trendingMovies = trendingMoviesResponse.data.movies,
                        topRatedMovies = topRatedMoviesResponse.data.movies,
                        upcomingMovies = upcomingMoviesResponse.data.movies,
                        movieDataStatus = MovieDataStatus.Success
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        movieDataStatus = MovieDataStatus.Error(
                            message = UiText.StringResource(R.string.movies_error)
                        )
                    )
                }
            }
        }
    }
}

data class MoviesUiState(
    val movieDataStatus: MovieDataStatus = MovieDataStatus.Loading,
    val trendingMovies: List<MovieContent> = emptyList(),
    val topRatedMovies: List<MovieContent> = emptyList(),
    val upcomingMovies: List<MovieContent> = emptyList()
)

sealed interface MovieDataStatus {
    data object Loading : MovieDataStatus
    data object Success : MovieDataStatus
    data class Error(val message: UiText) : MovieDataStatus
}
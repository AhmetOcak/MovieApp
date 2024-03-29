package com.ahmetocak.movies

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.helpers.Response
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.domain.movie.GetNowPlayingMoviesFirstPageUseCase
import com.ahmetocak.domain.movie.GetPopularMoviesFirstPageUseCase
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
    private val getNowPlayingMoviesFirstPageUseCase: GetNowPlayingMoviesFirstPageUseCase,
    private val getPopularMoviesFirstPageUseCase: GetPopularMoviesFirstPageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    init {
        getNowPlayingMovies()
        getPopularMovies()
    }

    private fun getNowPlayingMovies() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = getNowPlayingMoviesFirstPageUseCase()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(nowPlayingMoviesState = MovieState.OnDataLoaded(response.data.movies))
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(nowPlayingMoviesState = MovieState.OnError(response.errorMessage))
                    }
                }
            }
        }
    }

    private fun getPopularMovies() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = getPopularMoviesFirstPageUseCase()) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(popularMoviesState = MovieState.OnDataLoaded(response.data.movies))
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(popularMoviesState = MovieState.OnError(response.errorMessage))
                    }
                }
            }
        }
    }
}

data class MoviesUiState(
    val nowPlayingMoviesState: MovieState = MovieState.Loading,
    val popularMoviesState: MovieState = MovieState.Loading,
    val errorMessage: List<UiText> = emptyList()
)

@Stable
sealed class MovieState {
    data object Loading : MovieState()
    data class OnDataLoaded(val movieList: List<MovieContent> = emptyList()) : MovieState()
    data class OnError(val errorMessage: UiText? = null) : MovieState()
}
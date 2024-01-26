package com.ahmetocak.movieapp.presentation.home.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.data.repository.movie.MovieRepository
import com.ahmetocak.movieapp.model.movie.MovieContent
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
    private val movieRepository: MovieRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    init {
        getNowPlayingMovies()
    }

    private fun getNowPlayingMovies() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = movieRepository.getNowPlayingMoviesFirstPage()) {
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
}

data class MoviesUiState(
    val nowPlayingMoviesState: MovieState = MovieState.Loading,
    val popularMoviesState: MovieState = MovieState.Loading
)

sealed class MovieState {
    data object Loading : MovieState()
    data class OnDataLoaded(val movieList: List<MovieContent> = emptyList()) : MovieState()
    data class OnError(val errorMessage: UiText? = null) : MovieState()
}
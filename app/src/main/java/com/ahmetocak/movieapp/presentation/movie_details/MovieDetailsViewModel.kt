package com.ahmetocak.movieapp.presentation.movie_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.common.UiState
import com.ahmetocak.movieapp.data.repository.movie.MovieRepository
import com.ahmetocak.movieapp.domain.model.MovieCredit
import com.ahmetocak.movieapp.domain.model.MovieDetail
import com.ahmetocak.movieapp.model.movie_detail.MovieTrailer
import com.ahmetocak.movieapp.presentation.navigation.MainDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    init {
        val movieId = savedStateHandle.get<String>(MainDestinations.MOVIE_DETAILS_ID_KEY)

        movieId?.let { id ->
            getMovieDetails(id.toInt())
            getMovieCredits(id.toInt())
            getMovieTrailers(id.toInt())
        }
    }

    private fun getMovieDetails(movieId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = movieRepository.getMovieDetails(movieId)) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(detailUiState = UiState.OnDataLoaded(response.data))
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(detailUiState = UiState.OnError(response.errorMessage))
                    }
                }
            }
        }
    }

    private fun getMovieCredits(movieId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = movieRepository.getMovieCredits(movieId)) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            directorName = response.data.directorName,
                            castUiState = UiState.OnDataLoaded(response.data)
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(castUiState = UiState.OnError(response.errorMessage))
                    }
                }
            }
        }
    }

    private fun getMovieTrailers(movieId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = movieRepository.getMovieTrailers(movieId)) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(trailersUiState = UiState.OnDataLoaded(response.data))
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(trailersUiState = UiState.OnError(response.errorMessage))
                    }
                }
            }
        }
    }
}

data class MovieDetailUiState(
    val detailUiState: UiState<MovieDetail> = UiState.Loading,
    val castUiState: UiState<MovieCredit> = UiState.Loading,
    val directorName: String = "",
    val trailersUiState: UiState<MovieTrailer> = UiState.Loading
)
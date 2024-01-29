package com.ahmetocak.movieapp.presentation.movie_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.common.UiState
import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.movieapp.data.repository.movie.MovieRepository
import com.ahmetocak.movieapp.domain.model.MovieCredit
import com.ahmetocak.movieapp.domain.model.MovieDetail
import com.ahmetocak.movieapp.domain.usecase.firebase.DeleteMovieFromWatchListUseCase
import com.ahmetocak.movieapp.model.firebase.firestore.WatchListMovie
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
    savedStateHandle: SavedStateHandle,
    private val firebaseRepository: FirebaseRepository,
    private val deleteMovieFromWatchListUseCase: DeleteMovieFromWatchListUseCase
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

    fun addMovieToFirestore(watchListMovie: WatchListMovie) {
        _uiState.update {
            it.copy(isWatchlistButtonInProgress = true)
        }
        viewModelScope.launch(ioDispatcher) {
            firebaseRepository.addMovieToFirestore(watchListMovie).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isWatchlistButtonInProgress = false,
                            isMovieInWatchList = true,
                            userMessages = listOf(
                                UiText.StringResource(R.string.movie_add_watch_list)
                            )
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            userMessages = listOf(
                                task.exception?.message?.let { message ->
                                    UiText.DynamicString(message)
                                } ?: kotlin.run {
                                    UiText.StringResource(R.string.unknown_error)
                                }
                            ),
                            isWatchlistButtonInProgress = false
                        )
                    }
                }
            }
        }
    }

    fun deleteMovieFromTheFirestore(watchListMovie: WatchListMovie) {
        _uiState.update {
            it.copy(isWatchlistButtonInProgress = true)
        }
        viewModelScope.launch(ioDispatcher) {
            deleteMovieFromWatchListUseCase(
                watchListMovie = watchListMovie,
                onTaskSuccess = {
                    _uiState.update {
                        it.copy(
                            isWatchlistButtonInProgress = false,
                            isMovieInWatchList = false,
                            userMessages = listOf(
                                UiText.StringResource(R.string.movie_remove_watch_list)
                            )
                        )
                    }
                },
                onTaskError = { errorMessage ->
                    _uiState.update {
                        it.copy(
                            isWatchlistButtonInProgress = false,
                            userMessages = listOf(
                                errorMessage?.let { message ->
                                    UiText.DynamicString(message)
                                } ?: kotlin.run {
                                    UiText.StringResource(R.string.unknown_error)
                                }
                            )
                        )
                    }
                }
            )
        }
    }

    fun consumedUserMessage() {
        _uiState.update {
            it.copy(userMessages = emptyList())
        }
    }
}

data class MovieDetailUiState(
    val detailUiState: UiState<MovieDetail> = UiState.Loading,
    val castUiState: UiState<MovieCredit> = UiState.Loading,
    val directorName: String = "",
    val trailersUiState: UiState<MovieTrailer> = UiState.Loading,
    val userMessages: List<UiText> = emptyList(),
    val isWatchlistButtonInProgress: Boolean = false,
    val isMovieInWatchList: Boolean = false
)
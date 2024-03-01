package com.ahmetocak.movie_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.helpers.Response
import com.ahmetocak.common.helpers.UiState
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.common.helpers.handleTaskError
import com.ahmetocak.domain.DeleteMovieFromWatchListUseCase
import com.ahmetocak.domain.firebase.CheckMovieInWatchListUseCase
import com.ahmetocak.domain.firebase.firestore.AddMovieToWatchListUseCase
import com.ahmetocak.domain.movie.AddMovieToDbWatchListUseCase
import com.ahmetocak.domain.movie.GetMovieCreditsUseCase
import com.ahmetocak.domain.movie.GetMovieDetailsUseCase
import com.ahmetocak.domain.movie.GetMovieTrailersUseCase
import com.ahmetocak.model.firebase.WatchListMovie
import com.ahmetocak.model.movie_detail.MovieCredit
import com.ahmetocak.model.movie_detail.MovieDetail
import com.ahmetocak.model.movie_detail.MovieTrailer
import com.ahmetocak.navigation.MainDestinations
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
    private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
    private val deleteMovieFromWatchListUseCase: DeleteMovieFromWatchListUseCase,
    private val checkMovieInWatchListUseCase: CheckMovieInWatchListUseCase,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieCreditsUseCase: GetMovieCreditsUseCase,
    private val getMovieTrailersUseCase: GetMovieTrailersUseCase,
    private val addMovieToWatchListUseCase: AddMovieToWatchListUseCase,
    private val addMovieToDbWatchListUseCase: AddMovieToDbWatchListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    init {
        val movieId = savedStateHandle.get<String>(MainDestinations.MOVIE_DETAILS_ID_KEY)

        movieId?.let { id ->
            getMovieDetails(id.toInt())
            getMovieCredits(id.toInt())
            getMovieTrailers(id.toInt())
            isMovieInWatchList(id.toInt())
        }
    }

    private fun getMovieDetails(movieId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = getMovieDetailsUseCase(movieId)) {
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
            when (val response = getMovieCreditsUseCase(movieId)) {
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
            when (val response = getMovieTrailersUseCase(movieId)) {
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

    fun handleWatchListAction(watchListMovie: WatchListMovie) {
        if (_uiState.value.isMovieInWatchList) {
            removeMovieFromTheWatchList(watchListMovie)
        } else {
            addMovieToWatchList(watchListMovie)
        }
    }

    private fun addMovieToWatchList(watchListMovie: WatchListMovie) {
        _uiState.update {
            it.copy(isWatchlistButtonInProgress = true)
        }
        viewModelScope.launch(ioDispatcher) {
            addMovieToWatchListUseCase(watchListMovie).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    addMovieToWatchListDb(watchListMovie)
                } else {
                    _uiState.update {
                        it.copy(
                            userMessages = listOf(handleTaskError(task.exception)),
                            isWatchlistButtonInProgress = false
                        )
                    }
                }
            }
        }
    }

    private fun addMovieToWatchListDb(watchListMovie: WatchListMovie) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = addMovieToDbWatchListUseCase(watchListMovie)) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            isWatchlistButtonInProgress = false,
                            isMovieInWatchList = true,
                            userMessages = listOf(
                                UiText.StringResource(R.string.movie_add_watch_list)
                            )
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(userMessages = listOf(response.errorMessage))
                    }
                }
            }
        }
    }

    private fun removeMovieFromTheWatchList(watchListMovie: WatchListMovie) {
        _uiState.update {
            it.copy(isWatchlistButtonInProgress = true)
        }
        viewModelScope.launch(ioDispatcher) {
            watchListMovie.id?.let {
                deleteMovieFromWatchListUseCase(
                    movieId = it,
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
                                userMessages = listOf(errorMessage)
                            )
                        }
                    }
                )
            }
        }
    }

    private fun isMovieInWatchList(movieId: Int) {
        _uiState.update {
            it.copy(isWatchlistButtonInProgress = true)
        }
        viewModelScope.launch(ioDispatcher) {
            checkMovieInWatchListUseCase(
                movieId = movieId,
                onSuccess = { isMovieInWatchList ->
                    _uiState.update {
                        it.copy(
                            isWatchlistButtonInProgress = false,
                            isMovieInWatchList = isMovieInWatchList
                        )
                    }
                },
                onError = { errorMessage ->
                    _uiState.update {
                        it.copy(
                            isWatchlistButtonInProgress = false,
                            userMessages = listOf(errorMessage)
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
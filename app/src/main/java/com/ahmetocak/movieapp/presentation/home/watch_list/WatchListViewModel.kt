package com.ahmetocak.movieapp.presentation.home.watch_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.data.repository.movie.MovieRepository
import com.ahmetocak.movieapp.domain.usecase.firebase.DeleteMovieFromWatchListUseCase
import com.ahmetocak.movieapp.model.firebase.firestore.WatchListMovie
import com.ahmetocak.movieapp.model.watch_list.WatchListEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchListViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val deleteMovieFromWatchListUseCase: DeleteMovieFromWatchListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchListUiState())
    val uiState: StateFlow<WatchListUiState> = _uiState.asStateFlow()

    init {
        getWatchList()
    }

    private fun getWatchList() {
        viewModelScope.launch {
            when (val response = movieRepository.getWatchList()) {
                is Response.Success -> {
                    response.data.flowOn(ioDispatcher).collect { watchList ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                watchList = watchList
                            )
                        }
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, userMessages = listOf(response.errorMessage))
                    }
                }
            }
        }
    }

    fun deleteMovieFromWatchList(watchListMovie: WatchListMovie) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(ioDispatcher) {
            deleteMovieFromWatchListUseCase.invoke(
                watchListMovie = watchListMovie,
                onTaskSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userMessages = listOf(UiText.StringResource(R.string.movie_remove_watch_list))
                        )
                    }
                },
                onTaskError = { errorMessage ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userMessages = listOf(errorMessage)
                        )
                    }
                }
            )
        }
    }

    fun onUserMessageConsumed() {
        _uiState.update {
            it.copy(userMessages = emptyList())
        }
    }
}

data class WatchListUiState(
    val isLoading: Boolean = true,
    val userMessages: List<UiText> = emptyList(),
    val watchList: List<WatchListEntity> = emptyList()
)
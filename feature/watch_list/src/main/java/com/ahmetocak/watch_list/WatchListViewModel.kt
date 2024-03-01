package com.ahmetocak.watch_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.helpers.Response
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.domain.DeleteMovieFromWatchListUseCase
import com.ahmetocak.domain.movie.GetWatchListUseCase
import com.ahmetocak.model.firebase.WatchListMovie
import com.ahmetocak.model.watch_list.WatchList
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
    private val getWatchListUseCase: GetWatchListUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val deleteMovieFromWatchListUseCase: DeleteMovieFromWatchListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchListUiState())
    val uiState: StateFlow<WatchListUiState> = _uiState.asStateFlow()

    init {
        getWatchList()
    }

    private fun getWatchList() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = getWatchListUseCase()) {
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
        viewModelScope.launch(ioDispatcher) {
            watchListMovie.id?.let {
                deleteMovieFromWatchListUseCase.invoke(
                    movieId = it,
                    onTaskSuccess = {
                        _uiState.update {
                            it.copy(userMessages = listOf(UiText.StringResource(R.string.movie_remove_watch_list)))
                        }
                    },
                    onTaskError = { errorMessage ->
                        _uiState.update {
                            it.copy(userMessages = listOf(errorMessage))
                        }
                    }
                )
            }
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
    val watchList: List<WatchList> = emptyList()
)
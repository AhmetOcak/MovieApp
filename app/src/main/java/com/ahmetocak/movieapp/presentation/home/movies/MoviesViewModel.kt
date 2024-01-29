package com.ahmetocak.movieapp.presentation.home.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.movieapp.data.repository.movie.MovieRepository
import com.ahmetocak.movieapp.domain.mapper.toWatchListEntity
import com.ahmetocak.movieapp.model.firebase.firestore.WatchList
import com.ahmetocak.movieapp.model.movie.MovieContent
import com.ahmetocak.movieapp.model.watch_list.WatchListEntity
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
    private val ioDispatcher: CoroutineDispatcher,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    init {
        /*
        getNowPlayingMovies()
        getPopularMovies()
        getUserWatchListAndSaveToDatabase()
         */
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

    private fun getPopularMovies() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = movieRepository.getPopularMoviesFirstPage()) {
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

    private fun getUserWatchListAndSaveToDatabase() {
        viewModelScope.launch(ioDispatcher) {
            firebaseRepository.getMovieData().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        val watchList = document.toObject(WatchList::class.java)?.watchList
                        watchList?.forEach {
                            addMovieToWatchList(it.toWatchListEntity())
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = listOf(
                                task.exception?.message?.let { message ->
                                    UiText.DynamicString(message)
                                } ?: kotlin.run {
                                    UiText.StringResource(
                                        R.string.watch_list_get_error
                                    )
                                }
                            )
                        )
                    }
                }
            }
        }
    }

    private fun addMovieToWatchList(watchListEntity: WatchListEntity) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = movieRepository.addMovieToWatchList(watchListEntity)) {
                is Response.Success -> {}

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessage = listOf(response.errorMessage))
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

sealed class MovieState {
    data object Loading : MovieState()
    data class OnDataLoaded(val movieList: List<MovieContent> = emptyList()) : MovieState()
    data class OnError(val errorMessage: UiText? = null) : MovieState()
}
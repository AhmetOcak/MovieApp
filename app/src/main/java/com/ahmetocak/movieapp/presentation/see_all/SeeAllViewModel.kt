package com.ahmetocak.movieapp.presentation.see_all

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.data.repository.movie.MovieRepository
import com.ahmetocak.movieapp.model.movie.MovieContent
import com.ahmetocak.movieapp.presentation.navigation.MainDestinations
import com.ahmetocak.movieapp.utils.SeeAllType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SeeAllViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(SeeAllUiState())
    val uiState: StateFlow<SeeAllUiState> = _uiState.asStateFlow()

    init {
        val seeAllType = savedStateHandle.get<String>(MainDestinations.SEE_ALL_TYPE_KEY)
        _uiState.update {
            if (seeAllType == SeeAllType.NOW_PLAYING.toString()) {
                it.copy(
                    topBarTitle = UiText.StringResource(R.string.now_playing_text),
                    movieList = movieRepository.getAllNowPlayingMovies().cachedIn(viewModelScope)
                )
            } else {
                it.copy(
                    topBarTitle = UiText.StringResource(R.string.popular_movies_text),
                    movieList = movieRepository.getAllPopularMovies().cachedIn(viewModelScope)
                )
            }
        }
    }
}

data class SeeAllUiState(
    val movieList: Flow<PagingData<MovieContent>>? = null,
    val topBarTitle: UiText = UiText.DynamicString("")
)
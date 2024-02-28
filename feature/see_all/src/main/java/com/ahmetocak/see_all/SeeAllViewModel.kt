package com.ahmetocak.see_all

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ahmetocak.common.constants.SeeAllType
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.domain.movie.GetAllNowPlayingMoviesUseCase
import com.ahmetocak.domain.movie.GetAllPopularMoviesUseCase
import com.ahmetocak.model.movie.MovieContent
import com.ahmetocak.navigation.MainDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SeeAllViewModel @Inject constructor(
    private val getAllPopularMoviesUseCase: GetAllPopularMoviesUseCase,
    private val getAllNowPlayingMoviesUseCase: GetAllNowPlayingMoviesUseCase,
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
                    movieList = getAllNowPlayingMoviesUseCase().cachedIn(viewModelScope)
                )
            } else {
                it.copy(
                    topBarTitle = UiText.StringResource(R.string.popular_movies_text),
                    movieList = getAllPopularMoviesUseCase().cachedIn(viewModelScope)
                )
            }
        }
    }
}

data class SeeAllUiState(
    val movieList: Flow<PagingData<MovieContent>> = emptyFlow(),
    val topBarTitle: UiText = UiText.DynamicString("")
)
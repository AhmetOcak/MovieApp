package com.ahmetocak.see_all

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ahmetocak.common.constants.SeeAllType
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.domain.movie.GetAllTopRatedMoviesUseCase
import com.ahmetocak.domain.movie.GetAllUpcomingMoviesUseCase
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
import com.ahmetocak.common.R
import com.ahmetocak.domain.movie.GetAllTrendingMoviesUseCase

@HiltViewModel
class SeeAllViewModel @Inject constructor(
    private val getAllTrendingMoviesUseCase: GetAllTrendingMoviesUseCase,
    private val getAllUpcomingMoviesUseCase: GetAllUpcomingMoviesUseCase,
    private val getAllTopRatedMoviesUseCase: GetAllTopRatedMoviesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(SeeAllUiState())
    val uiState: StateFlow<SeeAllUiState> = _uiState.asStateFlow()

    init {
        val seeAllType = savedStateHandle.get<String>(MainDestinations.SEE_ALL_TYPE_KEY)

        when(seeAllType) {
            SeeAllType.TRENDING.toString() -> {
                _uiState.update {
                    it.copy(
                        topBarTitle = UiText.StringResource(R.string.trending),
                        movieList = getAllTrendingMoviesUseCase().cachedIn(viewModelScope)
                    )
                }
            }

            SeeAllType.TOP_RATED.toString() -> {
                _uiState.update {
                    it.copy(
                        topBarTitle = UiText.StringResource(R.string.top_rated),
                        movieList = getAllTopRatedMoviesUseCase().cachedIn(viewModelScope)
                    )
                }
            }

            SeeAllType.UPCOMING.toString() -> {
                _uiState.update {
                    it.copy(
                        topBarTitle = UiText.StringResource(R.string.upcoming),
                        movieList = getAllUpcomingMoviesUseCase().cachedIn(viewModelScope)
                    )
                }
            }
        }
    }
}

data class SeeAllUiState(
    val movieList: Flow<PagingData<MovieContent>> = emptyFlow(),
    val topBarTitle: UiText = UiText.DynamicString("")
)
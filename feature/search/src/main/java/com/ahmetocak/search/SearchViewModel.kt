package com.ahmetocak.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.domain.movie.SearchMovieUseCase
import com.ahmetocak.model.movie.MovieContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMovieUseCase: SearchMovieUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    var query by mutableStateOf("")
        private set

    fun updateQueryValue(value: String) {
        query = value
    }

    fun searchMovie() {
        if (query.isNotBlank()) {
            _uiState.update {
                it.copy(
                    queryFieldErrorMessage = null,
                    isSearchDone = true,
                    searchResult = searchMovieUseCase(query).cachedIn(viewModelScope)
                )
            }
        } else {
            _uiState.update {
                it.copy(queryFieldErrorMessage = UiText.StringResource(R.string.blank_field))
            }
        }
    }
}

data class SearchUiState(
    val isSearchDone: Boolean = false,
    val searchResult: Flow<PagingData<MovieContent>> = emptyFlow(),
    val queryFieldErrorMessage: UiText? = null
)
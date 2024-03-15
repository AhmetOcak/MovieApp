package com.ahmetocak.actor_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.helpers.Response
import com.ahmetocak.common.helpers.UiState
import com.ahmetocak.domain.movie.GetActorDetailsUseCase
import com.ahmetocak.domain.movie.GetActorMoviesUseCase
import com.ahmetocak.model.movie.ActorDetails
import com.ahmetocak.model.movie.ActorMoviesContent
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
class ActorDetailsViewModel @Inject constructor(
    private val getActorDetailsUseCase: GetActorDetailsUseCase,
    private val getActorMoviesUseCase: GetActorMoviesUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActorDetailsUiState())
    val uiState: StateFlow<ActorDetailsUiState> = _uiState.asStateFlow()

    init {
        val actorId = savedStateHandle.get<String>(MainDestinations.ACTOR_DETAILS_ID_KEY)?.toInt()
        actorId?.let {
            getActorDetails(it)
            getActorMovies(it)
        }
    }

    private fun getActorDetails(actorId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = getActorDetailsUseCase(actorId)) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            actorName = response.data.name,
                            actorDetailsState = UiState.OnDataLoaded(response.data)
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(actorDetailsState = UiState.OnError(response.errorMessage))
                    }
                }
            }
        }
    }

    private fun getActorMovies(actorId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = getActorMoviesUseCase(actorId)) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            actorMoviesState = UiState.OnDataLoaded(
                                response.data.movies.filter {
                                    movie -> movie.posterImagePath != null
                                }
                            )
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(actorMoviesState = UiState.OnError(response.errorMessage))
                    }
                }
            }
        }
    }
}

data class ActorDetailsUiState(
    val actorName: String = "",
    val actorDetailsState: UiState<ActorDetails> = UiState.Loading,
    val actorMoviesState: UiState<List<ActorMoviesContent>> = UiState.Loading
)
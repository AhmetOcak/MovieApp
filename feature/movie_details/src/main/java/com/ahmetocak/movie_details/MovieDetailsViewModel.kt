package com.ahmetocak.movie_details

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.palette.graphics.Palette
import com.ahmetocak.common.helpers.Response
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.common.helpers.handleTaskError
import com.ahmetocak.domain.DeleteMovieFromWatchListUseCase
import com.ahmetocak.domain.GetGeminiResponseUseCase
import com.ahmetocak.domain.firebase.CheckMovieInWatchListUseCase
import com.ahmetocak.domain.firebase.firestore.AddMovieToWatchListUseCase
import com.ahmetocak.domain.movie.AddMovieToDbWatchListUseCase
import com.ahmetocak.domain.movie.GetMovieCreditsUseCase
import com.ahmetocak.domain.movie.GetMovieDetailsUseCase
import com.ahmetocak.domain.movie.GetMovieRecommendationsUseCase
import com.ahmetocak.domain.movie.GetMovieTrailersUseCase
import com.ahmetocak.domain.movie.GetUserMovieReviewsUseCase
import com.ahmetocak.model.firebase.WatchListMovie
import com.ahmetocak.model.movie.RecommendedMovieContent
import com.ahmetocak.model.movie.UserReviewResults
import com.ahmetocak.model.movie_detail.Cast
import com.ahmetocak.model.movie_detail.MovieDetail
import com.ahmetocak.model.movie_detail.Trailer
import com.ahmetocak.navigation.MainDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
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
    private val addMovieToDbWatchListUseCase: AddMovieToDbWatchListUseCase,
    private val getUserMovieReviewsUseCase: GetUserMovieReviewsUseCase,
    private val getMovieRecommendationsUseCase: GetMovieRecommendationsUseCase,
    private val getGeminiResponseUseCase: GetGeminiResponseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    private var movieName: String = ""

    init {
        val movieId = savedStateHandle.get<String>(MainDestinations.MOVIE_DETAILS_ID_KEY)

        movieId?.let { id ->
            isMovieInWatchList(id.toInt())
            getAllDataAndUpdateUi(id.toInt())
        }
    }

    private fun getAllDataAndUpdateUi(movieId: Int) {
        viewModelScope.launch(ioDispatcher) {
            val movieDetailsDeferred = async { getMovieDetailsUseCase(movieId) }
            val movieCreditsDeferred = async { getMovieCreditsUseCase(movieId) }
            val movieTrailersDeferred = async { getMovieTrailersUseCase(movieId) }

            val movieDetailsResponse = movieDetailsDeferred.await()
            val movieCreditsResponse = movieCreditsDeferred.await()
            val movieTrailersResponse = movieTrailersDeferred.await()

            if (movieDetailsResponse is Response.Success &&
                movieCreditsResponse is Response.Success &&
                movieTrailersResponse is Response.Success
            ) {
                _uiState.update {
                    it.copy(
                        movieDetails = listOf(movieDetailsResponse.data),
                        movieCast = movieCreditsResponse.data.cast,
                        movieTrailers = movieTrailersResponse.data.trailers,
                        userReviews = getUserMovieReviewsUseCase(movieId),
                        movieRecommendations = getMovieRecommendationsUseCase(movieId),
                        directorName = movieCreditsResponse.data.directorName,
                        movieDataStatus = MovieDataStatus.Success
                    )
                }

                movieName = movieDetailsResponse.data.movieName
            } else {
                _uiState.update {
                    it.copy(
                        movieDataStatus = MovieDataStatus.Error(
                            message = UiText.StringResource(R.string.movie_data_error)
                        )
                    )
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
            watchListMovie.id?.let { movieId ->
                deleteMovieFromWatchListUseCase(
                    movieId = movieId,
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

    fun getGeminiResponse(context: Context) {
        if (_uiState.value.gemini.responseString == null) {
            _uiState.update {
                it.copy(gemini = Gemini(isLoading = true))
            }
            viewModelScope.launch(ioDispatcher) {
                when (val response = getGeminiResponseUseCase(context.getString(
                        R.string.gemini_prompt,
                        movieName
                    ))) {
                    is Response.Success -> {
                        _uiState.update {
                            it.copy(
                                gemini = Gemini(
                                    isLoading = false,
                                    responseString = response.data
                                )
                            )
                        }
                    }

                    is Response.Error -> {
                        _uiState.update {
                            it.copy(
                                gemini = Gemini(
                                    isLoading = false,
                                    errorMessage = response.errorMessage
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun generatePaletteFromImage(bitmap: Bitmap) {
        val argbList = mutableListOf<Color>()

        Palette.from(bitmap).generate { palette ->
            palette?.apply {
                lightVibrantSwatch?.rgb?.let { argbList.add(Color(it)) }
                vibrantSwatch?.rgb?.let { argbList.add(Color(it)) }
                darkVibrantSwatch?.rgb?.let { argbList.add(Color(it)) }
                lightMutedSwatch?.rgb?.let { argbList.add(Color(it)) }
                mutedSwatch?.rgb?.let { argbList.add(Color(it)) }
                darkMutedSwatch?.rgb?.let { argbList.add(Color(it)) }
            }

            // There must be at least 2 colors in the gradient list.
            if (argbList.size == 0) {
                argbList.add(Color.Transparent)
                argbList.add(Color.Transparent)
            } else if (argbList.size == 1) {
                argbList.add(Color.Transparent)
            }

            _uiState.update {
                it.copy(posterBackgroundColors = argbList)
            }
        }
    }

    fun consumedUserMessage() {
        _uiState.update {
            it.copy(userMessages = emptyList())
        }
    }
}

data class MovieDetailUiState(
    val movieDetails: List<MovieDetail> = emptyList(),
    val movieTrailers: List<Trailer> = emptyList(),
    val movieCast: List<Cast> = emptyList(),
    val userReviews: Flow<PagingData<UserReviewResults>> = emptyFlow(),
    val movieRecommendations: Flow<PagingData<RecommendedMovieContent>> = emptyFlow(),
    val movieDataStatus: MovieDataStatus = MovieDataStatus.Loading,
    val directorName: String = "",
    val userMessages: List<UiText> = emptyList(),
    val isWatchlistButtonInProgress: Boolean = false,
    val isMovieInWatchList: Boolean = false,
    val gemini: Gemini = Gemini(),
    val posterBackgroundColors: List<Color> = listOf(
        Color.Transparent,
        Color.Transparent
    )
)

sealed interface MovieDataStatus {
    data object Loading: MovieDataStatus
    data object Success: MovieDataStatus
    data class Error(val message: UiText): MovieDataStatus
}

data class Gemini(
    val isLoading: Boolean = false,
    val responseString: String? = null,
    val errorMessage: UiText? = null
)
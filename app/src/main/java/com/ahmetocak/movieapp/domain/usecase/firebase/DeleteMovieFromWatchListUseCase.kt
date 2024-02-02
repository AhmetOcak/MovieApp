package com.ahmetocak.movieapp.domain.usecase.firebase

import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.movieapp.data.repository.movie.MovieRepository
import com.ahmetocak.movieapp.model.firebase.firestore.WatchListMovie
import com.ahmetocak.movieapp.utils.NetworkConnectivityObserver
import javax.inject.Inject

class DeleteMovieFromWatchListUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val movieRepository: MovieRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) {
    suspend operator fun invoke(
        watchListMovie: WatchListMovie,
        onTaskSuccess: () -> Unit,
        onTaskError: (UiText) -> Unit
    ) {
        if (networkConnectivityObserver.isNetworkAvailable()) {
            watchListMovie.id?.let { movieId ->
                when (val response = movieRepository.removeMovieFromWatchList(movieId)) {
                    is Response.Success -> {
                        firebaseRepository.removeMovieData(watchListMovie)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    onTaskSuccess()
                                } else {
                                    onTaskError(
                                        task.exception?.message?.let { message ->
                                            UiText.DynamicString(message)
                                        } ?: kotlin.run {
                                            UiText.StringResource(R.string.unknown_error)
                                        }
                                    )
                                }
                            }
                    }

                    is Response.Error -> {
                        onTaskError(response.errorMessage)
                    }
                }
            }
        }
    }
}
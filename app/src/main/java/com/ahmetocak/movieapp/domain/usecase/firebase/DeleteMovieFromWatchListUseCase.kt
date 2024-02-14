package com.ahmetocak.movieapp.domain.usecase.firebase

import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.common.helpers.connectivity.NetworkConnectivityObserver
import com.ahmetocak.movieapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.movieapp.data.repository.movie.MovieRepository
import com.ahmetocak.movieapp.model.firebase.firestore.WatchList
import com.ahmetocak.movieapp.utils.taskHandler
import javax.inject.Inject

class DeleteMovieFromWatchListUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val movieRepository: MovieRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) {
    suspend operator fun invoke(
        movieId: Int,
        onTaskSuccess: () -> Unit,
        onTaskError: (UiText) -> Unit
    ) {
        if (networkConnectivityObserver.isNetworkAvailable()) {
            when (val response = movieRepository.removeMovieFromWatchList(movieId)) {
                is Response.Success -> {
                    taskHandler(
                        taskCall = firebaseRepository.getMovieData(),
                        onTaskSuccess = { document ->
                            if (document != null) {
                                val watchList = document.toObject(WatchList::class.java)?.watchList ?: emptyList()
                                val updatedWatchList = watchList.filter { movie ->
                                    movie.id != movieId
                                }
                                taskHandler(
                                    taskCall = firebaseRepository.updateMovieData(updatedWatchList),
                                    onTaskSuccess = { onTaskSuccess() },
                                    onTaskError = onTaskError::invoke
                                )
                            } else {
                                onTaskError(UiText.StringResource(R.string.unknown_error))
                            }
                        },
                        onTaskError = onTaskError::invoke
                    )
                }

                is Response.Error -> {
                    onTaskError(response.errorMessage)
                }
            }
        }
    }
}
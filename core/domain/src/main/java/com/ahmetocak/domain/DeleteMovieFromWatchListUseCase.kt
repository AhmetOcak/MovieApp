package com.ahmetocak.domain

import com.ahmetocak.common.R
import com.ahmetocak.common.helpers.Response
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.common.helpers.handleTaskError
import com.ahmetocak.data.utils.NetworkConnectivityObserver
import com.ahmetocak.domain.firebase.firestore.GetMovieDataUseCase
import com.ahmetocak.domain.firebase.firestore.UpdateMovieDataUseCase
import com.ahmetocak.domain.movie.RemoveMovieFromWatchListUseCase
import com.ahmetocak.model.firebase.WatchList
import javax.inject.Inject

class DeleteMovieFromWatchListUseCase @Inject constructor(
    private val getMovieDataUseCase: GetMovieDataUseCase,
    private val updateMovieDataUseCase: UpdateMovieDataUseCase,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val removeMovieFromWatchListUseCase: RemoveMovieFromWatchListUseCase
) {
    suspend operator fun invoke(
        movieId: Int,
        onTaskSuccess: () -> Unit,
        onTaskError: (UiText) -> Unit
    ) {
        if (networkConnectivityObserver.isNetworkAvailable()) {
            when (val response = removeMovieFromWatchListUseCase(movieId)) {
                is Response.Success -> {
                    getMovieDataUseCase().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document != null) {
                                val watchList = document.toObject(WatchList::class.java)?.watchList
                                    ?: emptyList()
                                val updatedWatchList = watchList.filter { movie ->
                                    movie.id != movieId
                                }
                                updateMovieDataUseCase(updatedWatchList).addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        onTaskSuccess()
                                    } else {
                                        handleTaskError(updateTask.exception)
                                    }
                                }
                            } else {
                                onTaskError(UiText.StringResource(R.string.unknown_error))
                            }
                        } else {
                            handleTaskError(task.exception)
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
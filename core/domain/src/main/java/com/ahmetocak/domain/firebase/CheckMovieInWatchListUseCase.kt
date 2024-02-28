package com.ahmetocak.domain.firebase

import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.common.helpers.handleTaskError
import com.ahmetocak.domain.firebase.firestore.GetMovieDataUseCase
import com.ahmetocak.model.firebase.WatchList
import javax.inject.Inject

class CheckMovieInWatchListUseCase @Inject constructor(
    private val getMovieDataUseCase: GetMovieDataUseCase
) {

    operator fun invoke(
        movieId: Int,
        onError: (UiText) -> Unit,
        onSuccess: (Boolean) -> Unit
    ) {
        getMovieDataUseCase().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result != null && task.result.exists()) {
                    val watchList = task.result.toObject(WatchList::class.java)?.watchList ?: emptyList()

                    val movieInWatchList = watchList.firstOrNull { movie ->
                        movie.id == movieId
                    }

                    onSuccess(movieInWatchList != null)
                } else {
                    onSuccess(false)
                }
            } else {
                onError(handleTaskError(task.exception))
            }
        }
    }
}
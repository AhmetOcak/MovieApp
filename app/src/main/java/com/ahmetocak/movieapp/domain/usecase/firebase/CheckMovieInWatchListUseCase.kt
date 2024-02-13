package com.ahmetocak.movieapp.domain.usecase.firebase

import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.movieapp.model.firebase.firestore.WatchList
import com.ahmetocak.movieapp.utils.taskHandler
import javax.inject.Inject

class CheckMovieInWatchListUseCase @Inject constructor(private val repository: FirebaseRepository) {

    operator fun invoke(
        movieId: Int,
        onError: (UiText) -> Unit,
        onSuccess: (Boolean) -> Unit
    ) {
        taskHandler(
            taskCall = repository.getMovieData(),
            onTaskSuccess = { document ->
                if (document != null && document.exists()) {
                    val watchList = document.toObject(WatchList::class.java)?.watchList ?: emptyList()

                    val movieInWatchList = watchList.firstOrNull { movie ->
                        movie.id == movieId
                    }

                    onSuccess(movieInWatchList != null)
                } else {
                    onSuccess(false)
                }
            },
            onTaskError = onError::invoke
        )
    }
}
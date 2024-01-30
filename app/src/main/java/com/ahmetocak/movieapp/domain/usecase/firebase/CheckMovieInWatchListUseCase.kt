package com.ahmetocak.movieapp.domain.usecase.firebase

import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.movieapp.model.firebase.firestore.WatchList
import javax.inject.Inject

class CheckMovieInWatchListUseCase @Inject constructor(private val repository: FirebaseRepository) {

    operator fun invoke(
        movieId: Int,
        onError: (UiText) -> Unit,
        onSuccess: (Boolean) -> Unit
    ) {
        repository.getMovieData().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    val watchList =
                        document.toObject(WatchList::class.java)?.watchList ?: emptyList()

                    val movieInWatchList = watchList.firstOrNull { movie ->
                        movie.id == movieId
                    }

                    onSuccess(movieInWatchList != null)
                }
            } else {
                onError(
                    task.exception?.message?.let { message -> UiText.DynamicString(message) }
                        ?: UiText.StringResource(R.string.unknown_error)
                )
            }
        }
    }
}
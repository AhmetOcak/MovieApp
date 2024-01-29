package com.ahmetocak.movieapp.domain.usecase.firebase

import com.ahmetocak.movieapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.movieapp.model.firebase.firestore.WatchListMovie
import javax.inject.Inject

class DeleteMovieFromWatchListUseCase @Inject constructor(private val repository: FirebaseRepository) {

    operator fun invoke(
        watchListMovie: WatchListMovie,
        onTaskSuccess: () -> Unit,
        onTaskError: (String?) -> Unit
    ) {
        repository.removeMovieData(watchListMovie).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onTaskSuccess()
            } else {
                onTaskError(task.exception?.message)
            }
        }
    }
}
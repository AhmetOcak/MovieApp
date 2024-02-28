package com.ahmetocak.domain.firebase.firestore

import com.ahmetocak.data.repository.firebase.FirebaseRepository
import com.ahmetocak.model.firebase.WatchListMovie
import javax.inject.Inject

class UpdateMovieDataUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {

    operator fun invoke(watchListMovie: List<WatchListMovie>) =
        firebaseRepository.updateMovieData(watchListMovie = watchListMovie)
}
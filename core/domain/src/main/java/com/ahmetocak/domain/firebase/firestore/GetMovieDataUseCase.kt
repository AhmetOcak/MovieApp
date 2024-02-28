package com.ahmetocak.domain.firebase.firestore

import com.ahmetocak.data.repository.firebase.FirebaseRepository
import javax.inject.Inject

class GetMovieDataUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {

    operator fun invoke() = firebaseRepository.getMovieData()
}
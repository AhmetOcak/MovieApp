package com.ahmetocak.domain.firebase.auth

import com.ahmetocak.data.repository.firebase.FirebaseRepository
import javax.inject.Inject

class GetUserEmailUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {

    operator fun invoke() = firebaseRepository.getUserEmail()
}
package com.ahmetocak.domain.firebase.auth

import com.ahmetocak.data.repository.firebase.FirebaseRepository
import com.ahmetocak.model.firebase.Auth
import javax.inject.Inject

class ReAuthenticateUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {

    operator fun invoke(auth: Auth) = firebaseRepository.reAuthenticate(auth = auth)
}
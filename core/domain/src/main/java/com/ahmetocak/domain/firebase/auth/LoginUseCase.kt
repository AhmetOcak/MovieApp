package com.ahmetocak.domain.firebase.auth

import com.ahmetocak.data.repository.firebase.FirebaseRepository
import com.ahmetocak.model.firebase.Auth
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {

    operator fun invoke(auth: Auth) = firebaseRepository.login(auth = auth)
}
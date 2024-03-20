package com.ahmetocak.domain.firebase.auth

import com.ahmetocak.authentication.firebase.FirebaseAuthClient
import javax.inject.Inject

class GetUserEmailUseCase @Inject constructor(private val firebaseAuthClient: FirebaseAuthClient) {

    operator fun invoke() = firebaseAuthClient.getUserEmail()
}
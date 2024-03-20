package com.ahmetocak.domain.firebase.auth

import com.ahmetocak.authentication.firebase.FirebaseAuthClient
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.common.helpers.handleTaskError
import javax.inject.Inject

class SendResetPasswordEmailUseCase @Inject constructor(private val firebaseAuthClient: FirebaseAuthClient) {

    operator fun invoke(email: String, onTaskSuccess: () -> Unit, onTaskFailed: (UiText) -> Unit) {
        firebaseAuthClient.sendResetPasswordEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onTaskSuccess()
            } else {
                onTaskFailed(handleTaskError(task.exception))
            }
        }
    }
}
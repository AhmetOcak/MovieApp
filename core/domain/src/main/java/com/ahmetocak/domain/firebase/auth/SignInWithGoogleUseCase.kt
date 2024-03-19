package com.ahmetocak.domain.firebase.auth

import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import com.ahmetocak.authentication.firebase.GoogleAuthClient
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.common.helpers.handleTaskError
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(private val googleAuthClient: GoogleAuthClient) {

    fun startSignInIntent(
        onTaskFailed: (UiText) -> Unit,
        onTaskSuccess: (IntentSenderRequest) -> Unit
    ) {
        googleAuthClient.startSignInIntent().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onTaskSuccess(
                    IntentSenderRequest.Builder(
                        task.result.pendingIntent.intentSender
                    ).build()
                )
            } else {
                onTaskFailed(handleTaskError(task.exception))
            }
        }
    }

    fun signInWithGoogle(
        intent: Intent,
        onTaskSuccess: () -> Unit,
        onTaskFailed: (UiText) -> Unit
    ) {
        googleAuthClient.signInWithIntent(intent).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onTaskSuccess()
            } else {
                onTaskFailed(handleTaskError(task.exception))
            }
        }
    }
}
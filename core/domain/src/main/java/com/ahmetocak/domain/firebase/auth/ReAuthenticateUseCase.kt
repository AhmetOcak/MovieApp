package com.ahmetocak.domain.firebase.auth

import com.ahmetocak.authentication.firebase.GoogleAuthClient
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.common.helpers.handleTaskError
import com.ahmetocak.data.repository.firebase.FirebaseRepository
import com.ahmetocak.model.firebase.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class ReAuthenticateUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val googleAuthClient: GoogleAuthClient
) {
    operator fun invoke(auth: Auth, onTaskSuccess: () -> Unit, onTaskFailed: (UiText) -> Unit) {
        val signInProvider = FirebaseAuth.getInstance().getAccessToken(false).result.signInProvider

        if (signInProvider == GoogleAuthProvider.PROVIDER_ID) {
            googleAuthClient.reAuthenticate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val account = task.result
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().currentUser?.reauthenticate(credential)
                        ?.addOnCompleteListener { reAuthTask ->
                            if (reAuthTask.isSuccessful) {
                                onTaskSuccess()
                            } else {
                                onTaskFailed(handleTaskError(reAuthTask.exception))
                            }
                        }
                } else {
                    onTaskFailed(handleTaskError(task.exception))
                }
            }
        } else {
            firebaseRepository.reAuthenticate(auth)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onTaskSuccess()
                } else {
                    onTaskFailed(handleTaskError(task.exception))
                }
            }
        }
    }
}
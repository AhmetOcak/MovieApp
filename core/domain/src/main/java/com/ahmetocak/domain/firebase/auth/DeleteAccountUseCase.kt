package com.ahmetocak.domain.firebase.auth

import com.ahmetocak.authentication.firebase.FirebaseAuthClient
import com.ahmetocak.common.helpers.Response
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.common.helpers.handleTaskError
import com.ahmetocak.data.repository.firebase.FirebaseRepository
import com.ahmetocak.data.repository.movie.MovieRepository
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val reAuthenticateUseCase: ReAuthenticateUseCase,
    private val firebaseRepository: FirebaseRepository,
    private val movieRepository: MovieRepository,
    private val firebaseAuthClient: FirebaseAuthClient
) {

    operator fun invoke(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailed: (UiText) -> Unit,
        scope: CoroutineScope
    ) {
        reAuthenticate(
            email = email,
            password = password,
            onTaskFailed = onFailed,
            onTaskSuccess = {
                deleteMovieDocument(
                    onTaskFailed = onFailed,
                    onSuccess = {
                        deleteUserProfileImage(
                            onTaskFailed = onFailed,
                            onSuccess = {
                                clearMovieLocalDatabase(
                                    onFailed = onFailed,
                                    scope = scope,
                                    onSuccess = {
                                        deleteAccount(
                                            onTaskFailed = onFailed,
                                            onSuccess = onSuccess
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
            }
        )
    }

    private fun reAuthenticate(
        email: String,
        password: String,
        onTaskSuccess: () -> Unit,
        onTaskFailed: (UiText) -> Unit
    ) {
        reAuthenticateUseCase(
            email = email,
            password = password,
            onTaskSuccess = onTaskSuccess,
            onTaskFailed = { errorMessage ->
                onTaskFailed(errorMessage)
            }
        )
    }

    private fun deleteMovieDocument(onSuccess: () -> Unit, onTaskFailed: (UiText) -> Unit) {
        firebaseRepository.deleteMovieDocument().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onTaskFailed(handleTaskError(task.exception))
            }
        }
    }

    private fun deleteUserProfileImage(onSuccess: () -> Unit, onTaskFailed: (UiText) -> Unit) {
        firebaseRepository.deleteUserProfileImage().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else if (task.exception is StorageException) {
                if ((task.exception as StorageException).errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    onSuccess()
                }
            } else {
                onTaskFailed(handleTaskError(task.exception))

            }
        }
    }

    private fun clearMovieLocalDatabase(
        onSuccess: () -> Unit,
        onFailed: (UiText) -> Unit,
        scope: CoroutineScope
    ) {
        scope.launch(Dispatchers.IO) {
            when (val response = movieRepository.deleteWatchList()) {
                is Response.Success -> {
                    onSuccess()
                }

                is Response.Error -> {
                    onFailed(response.errorMessage)
                }
            }
        }
    }

    private fun deleteAccount(onSuccess: () -> Unit, onTaskFailed: (UiText) -> Unit) {
        firebaseAuthClient.deleteAccount()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onTaskFailed(handleTaskError(task.exception))
            }
        }
    }
}
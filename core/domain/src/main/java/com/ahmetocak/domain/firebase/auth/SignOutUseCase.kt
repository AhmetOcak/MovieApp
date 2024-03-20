package com.ahmetocak.domain.firebase.auth

import com.ahmetocak.authentication.firebase.FirebaseAuthClient
import com.ahmetocak.common.helpers.Response
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.data.repository.movie.MovieRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val firebaseAuthClient: FirebaseAuthClient,
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(onFailed: (UiText) -> Unit) {
        when(val response = movieRepository.deleteWatchList()) {
            is Response.Success -> {
                firebaseAuthClient.signOut()
            }
            is Response.Error -> {
                onFailed(response.errorMessage)
            }
        }
    }
}
package com.ahmetocak.movieapp.data.repository.firebase

import com.ahmetocak.movieapp.model.firebase.auth.Auth
import com.ahmetocak.movieapp.model.firebase.firestore.WatchListMovie
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface FirebaseRepository {

    fun signUp(auth: Auth): Task<AuthResult>

    fun login(auth: Auth): Task<AuthResult>

    fun sendResetPasswordEmail(email: String): Task<Void>

    fun reAuthenticate(auth: Auth): Task<Void>?

    fun deleteAccount(): Task<Void>?

    fun addMovieToFirestore(watchListMovie: WatchListMovie): Task<Void>
}
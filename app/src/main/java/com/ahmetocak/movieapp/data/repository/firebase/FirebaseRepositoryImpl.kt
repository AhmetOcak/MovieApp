package com.ahmetocak.movieapp.data.repository.firebase

import com.ahmetocak.movieapp.data.datasource.remote.firebase.auth.FirebaseAuthDataSource
import com.ahmetocak.movieapp.data.datasource.remote.firebase.firestore.FirebaseFirestoreDataSource
import com.ahmetocak.movieapp.model.firebase.auth.Auth
import com.ahmetocak.movieapp.model.firebase.firestore.WatchListMovie
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val firebaseFirestoreDataSource: FirebaseFirestoreDataSource
) : FirebaseRepository {

    override fun signUp(auth: Auth): Task<AuthResult> = firebaseAuthDataSource.signUp(auth)

    override fun login(auth: Auth): Task<AuthResult> = firebaseAuthDataSource.login(auth)

    override fun sendResetPasswordEmail(email: String): Task<Void> =
        firebaseAuthDataSource.sendResetPasswordEmail(email)

    override fun reAuthenticate(auth: Auth): Task<Void>? =
        firebaseAuthDataSource.reAuthenticate(auth)

    override fun deleteAccount(): Task<Void>? = firebaseAuthDataSource.deleteAccount()
    override fun addMovieToFirestore(watchListMovie: WatchListMovie): Task<Void> =
        firebaseFirestoreDataSource.addMovieData(watchListMovie)
}
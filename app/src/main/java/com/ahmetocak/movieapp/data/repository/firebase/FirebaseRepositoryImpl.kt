package com.ahmetocak.movieapp.data.repository.firebase

import android.net.Uri
import com.ahmetocak.movieapp.data.datasource.remote.firebase.auth.FirebaseAuthDataSource
import com.ahmetocak.movieapp.data.datasource.remote.firebase.firestore.FirebaseFirestoreDataSource
import com.ahmetocak.movieapp.data.datasource.remote.firebase.storage.FirebaseStorageDataSource
import com.ahmetocak.movieapp.model.firebase.auth.Auth
import com.ahmetocak.movieapp.model.firebase.firestore.WatchListMovie
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val firebaseFirestoreDataSource: FirebaseFirestoreDataSource,
    private val firebaseStorageDataSource: FirebaseStorageDataSource
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

    override fun updateMovieData(watchListMovie: List<WatchListMovie>): Task<Void> =
        firebaseFirestoreDataSource.updateMovieData(watchListMovie)

    override fun getMovieData(): Task<DocumentSnapshot> = firebaseFirestoreDataSource.getMovieData()

    override fun deleteMovieDocument(): Task<Void> =
        firebaseFirestoreDataSource.deleteMovieDocument()

    override fun uploadProfileImage(imageUri: Uri): UploadTask =
        firebaseStorageDataSource.uploadProfileImage(imageUri)

    override fun getUserProfileImage(): Task<Uri> = firebaseStorageDataSource.getUserProfileImage()

    override fun deleteUserProfileImage(): Task<Void> =
        firebaseStorageDataSource.deleteUserProfileImage()
}
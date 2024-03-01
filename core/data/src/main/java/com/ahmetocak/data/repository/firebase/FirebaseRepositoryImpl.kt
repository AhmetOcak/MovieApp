package com.ahmetocak.data.repository.firebase

import android.net.Uri
import com.ahmetocak.data.mapper.toNetworkAuth
import com.ahmetocak.data.mapper.toNetworkWatchListMovie
import com.ahmetocak.model.firebase.Auth
import com.ahmetocak.model.firebase.WatchListMovie
import com.ahmetocak.network.datasource.firebase.auth.FirebaseAuthDataSource
import com.ahmetocak.network.datasource.firebase.firestore.FirebaseFirestoreDataSource
import com.ahmetocak.network.datasource.firebase.storage.FirebaseStorageDataSource
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

    override fun signUp(auth: Auth): Task<AuthResult> =
        firebaseAuthDataSource.signUp(auth.toNetworkAuth())

    override fun signOut() = firebaseAuthDataSource.signOut()

    override fun login(auth: Auth): Task<AuthResult> =
        firebaseAuthDataSource.login(auth.toNetworkAuth())

    override fun sendResetPasswordEmail(email: String): Task<Void> =
        firebaseAuthDataSource.sendResetPasswordEmail(email)

    override fun reAuthenticate(auth: Auth): Task<Void>? =
        firebaseAuthDataSource.reAuthenticate(auth.toNetworkAuth())

    override fun deleteAccount(): Task<Void>? = firebaseAuthDataSource.deleteAccount()

    override fun addMovieToFirestore(watchListMovie: WatchListMovie): Task<Void> =
        firebaseFirestoreDataSource.addMovieData(watchListMovie.toNetworkWatchListMovie())

    override fun updateMovieData(watchListMovie: List<WatchListMovie>): Task<Void> =
        firebaseFirestoreDataSource.updateMovieData(watchListMovie.toNetworkWatchListMovie())

    override fun getMovieData(): Task<DocumentSnapshot> = firebaseFirestoreDataSource.getMovieData()

    override fun deleteMovieDocument(): Task<Void> =
        firebaseFirestoreDataSource.deleteMovieDocument()

    override fun uploadProfileImage(imageUri: Uri): UploadTask =
        firebaseStorageDataSource.uploadProfileImage(imageUri)

    override fun getUserProfileImage(): Task<Uri> = firebaseStorageDataSource.getUserProfileImage()

    override fun deleteUserProfileImage(): Task<Void> =
        firebaseStorageDataSource.deleteUserProfileImage()

    override fun getUserEmail(): String? = firebaseAuthDataSource.getUserEmail()
}
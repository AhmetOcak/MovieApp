package com.ahmetocak.data.repository.firebase

import android.net.Uri
import com.ahmetocak.model.firebase.Auth
import com.ahmetocak.model.firebase.WatchListMovie
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.UploadTask

interface FirebaseRepository {

    fun signUp(auth: Auth): Task<AuthResult>

    fun signOut()

    fun login(auth: Auth): Task<AuthResult>

    fun sendResetPasswordEmail(email: String): Task<Void>

    fun reAuthenticate(auth: Auth): Task<Void>?

    fun deleteAccount(): Task<Void>?

    fun addMovieToFirestore(watchListMovie: WatchListMovie): Task<Void>

    fun updateMovieData(watchListMovie: List<WatchListMovie>): Task<Void>

    fun getMovieData(): Task<DocumentSnapshot>

    fun deleteMovieDocument(): Task<Void>

    fun uploadProfileImage(imageUri: Uri): UploadTask

    fun getUserProfileImage(): Task<Uri>

    fun deleteUserProfileImage(): Task<Void>

    fun getUserEmail(): String?
}
package com.ahmetocak.data.repository.firebase

import android.net.Uri
import com.ahmetocak.model.firebase.WatchListMovie
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.UploadTask

interface FirebaseRepository {

    fun addMovieToFirestore(watchListMovie: WatchListMovie): Task<Void>

    fun updateMovieData(watchListMovie: List<WatchListMovie>): Task<Void>

    fun getMovieData(): Task<DocumentSnapshot>

    fun deleteMovieDocument(): Task<Void>

    fun uploadProfileImage(imageUri: Uri): UploadTask

    fun getUserProfileImage(): Task<Uri>

    fun deleteUserProfileImage(): Task<Void>
}
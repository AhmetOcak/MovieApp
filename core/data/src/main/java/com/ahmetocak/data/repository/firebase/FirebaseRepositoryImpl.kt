package com.ahmetocak.data.repository.firebase

import android.net.Uri
import com.ahmetocak.data.mapper.toNetworkWatchListMovie
import com.ahmetocak.model.firebase.WatchListMovie
import com.ahmetocak.network.datasource.firebase.firestore.FirebaseFirestoreDataSource
import com.ahmetocak.network.datasource.firebase.storage.FirebaseStorageDataSource
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseFirestoreDataSource: FirebaseFirestoreDataSource,
    private val firebaseStorageDataSource: FirebaseStorageDataSource
) : FirebaseRepository {

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
}
package com.ahmetocak.movieapp.data.datasource.remote.firebase.storage

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask

interface FirebaseStorageDataSource {

    fun uploadProfileImage(imageUri: Uri): UploadTask

    fun getUserProfileImage(): Task<Uri>

    fun deleteUserProfileImage(): Task<Void>
}
package com.ahmetocak.movieapp.data.datasource.remote.firebase.storage

import android.net.Uri
import com.ahmetocak.movieapp.utils.Firestorage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

class FirebaseStorageDataSourceImpl @Inject constructor(
    firebaseStorage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth
) : FirebaseStorageDataSource {

    private val storageRef = firebaseStorage.reference


    override fun uploadProfileImage(imageUri: Uri): UploadTask {
        val profileImageRef =
            storageRef.child("${Firestorage.USER_PROFILE_IMG_PATH}/${firebaseAuth.currentUser?.uid}")
        return profileImageRef.putFile(imageUri)
    }

    override fun getUserProfileImage(): Task<Uri> {
        val profileImageRef =
            storageRef.child("${Firestorage.USER_PROFILE_IMG_PATH}/${firebaseAuth.currentUser?.uid}")
        return profileImageRef.downloadUrl
    }

    override fun deleteUserProfileImage(): Task<Void> {
        val profileImageRef =
            storageRef.child("${Firestorage.USER_PROFILE_IMG_PATH}/${firebaseAuth.currentUser?.uid}")
        return profileImageRef.delete()
    }
}
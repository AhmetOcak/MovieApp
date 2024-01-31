package com.ahmetocak.movieapp.data.datasource.remote.firebase.storage

import android.net.Uri
import com.ahmetocak.movieapp.utils.Firestorage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

class FirebaseStorageDataSourceImpl @Inject constructor(
    storageReference: StorageReference,
    firebaseAuth: FirebaseAuth
): FirebaseStorageDataSource {

    private val profileImageReference = storageReference
        .child("${Firestorage.USER_PROFILE_IMG_PATH}/${firebaseAuth.currentUser?.uid}")

    override fun uploadProfileImage(imageUri: Uri): UploadTask {
        return profileImageReference.putFile(imageUri)
    }

    override fun getUserProfileImage(): Task<Uri> {
        return profileImageReference.downloadUrl
    }

    override fun deleteUserProfileImage(): Task<Void> {
        return profileImageReference.delete()
    }
}
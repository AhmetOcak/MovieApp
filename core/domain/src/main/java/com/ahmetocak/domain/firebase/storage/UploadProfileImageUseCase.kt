package com.ahmetocak.domain.firebase.storage

import android.net.Uri
import com.ahmetocak.data.repository.firebase.FirebaseRepository
import javax.inject.Inject

class UploadProfileImageUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {

    operator fun invoke(imageUri: Uri) = firebaseRepository.uploadProfileImage(imageUri = imageUri)
}
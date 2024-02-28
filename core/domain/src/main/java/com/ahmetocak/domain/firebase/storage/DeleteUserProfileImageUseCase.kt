package com.ahmetocak.domain.firebase.storage

import com.ahmetocak.data.repository.firebase.FirebaseRepository
import javax.inject.Inject

class DeleteUserProfileImageUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {

    operator fun invoke() = firebaseRepository.deleteUserProfileImage()
}
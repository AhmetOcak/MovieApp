package com.ahmetocak.domain.firebase.storage

import com.ahmetocak.data.repository.firebase.FirebaseRepository
import javax.inject.Inject

class GetUserProfileImageUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {

    operator fun invoke() = firebaseRepository.getUserProfileImage()
}
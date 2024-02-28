package com.ahmetocak.domain.preferences

import com.ahmetocak.data.repository.datastore.DataStoreRepository
import javax.inject.Inject

class UpdateDynamicColorUseCase @Inject constructor(private val preferencesRepository: DataStoreRepository) {

    suspend operator fun invoke(isDynamicColor: Boolean) =
        preferencesRepository.updateDynamicColor(dynamicColor = isDynamicColor)
}
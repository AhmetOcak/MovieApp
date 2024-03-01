package com.ahmetocak.domain.preferences

import com.ahmetocak.data.repository.datastore.DataStoreRepository
import javax.inject.Inject

class UpdateAppThemeUseCase @Inject constructor(private val preferencesRepository: DataStoreRepository) {

    suspend operator fun invoke(isDarkMode: Boolean) =
        preferencesRepository.updateAppTheme(isDarkMode = isDarkMode)
}
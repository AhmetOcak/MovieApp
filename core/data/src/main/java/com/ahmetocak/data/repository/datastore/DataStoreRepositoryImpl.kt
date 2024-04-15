package com.ahmetocak.data.repository.datastore

import com.ahmetocak.datastore.datasource.MovieAppPreferenceDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val preferenceDataSource: MovieAppPreferenceDataSource
) : DataStoreRepository {

    override suspend fun observeAppTheme(): Flow<Boolean> = preferenceDataSource.observeAppTheme()

    override suspend fun updateAppTheme(isDarkMode: Boolean) =
        preferenceDataSource.updateAppTheme(isDarkMode)

    override suspend fun observeDynamicColor(): Flow<Boolean> = preferenceDataSource.observeDynamicColor()

    override suspend fun updateDynamicColor(dynamicColor: Boolean) =
        preferenceDataSource.updateDynamicColor(dynamicColor)
}
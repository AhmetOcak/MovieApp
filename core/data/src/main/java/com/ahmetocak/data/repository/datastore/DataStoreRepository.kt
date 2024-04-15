package com.ahmetocak.data.repository.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun observeAppTheme(): Flow<Boolean>

    suspend fun updateAppTheme(isDarkMode: Boolean)

    suspend fun observeDynamicColor(): Flow<Boolean>

    suspend fun updateDynamicColor(dynamicColor: Boolean)
}
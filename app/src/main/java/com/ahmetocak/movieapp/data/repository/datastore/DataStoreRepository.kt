package com.ahmetocak.movieapp.data.repository.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun getAppTheme(): Flow<Boolean>

    suspend fun updateAppTheme(isDarkMode: Boolean)

    suspend fun getDynamicColor(): Flow<Boolean>

    suspend fun updateDynamicColor(dynamicColor: Boolean)
}
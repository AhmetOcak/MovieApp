package com.ahmetocak.movieapp.data.repository.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun getRememberMe(): Boolean

    suspend fun updateRememberMe(value: Boolean)

    suspend fun getAppTheme(): Flow<Boolean>

    suspend fun updateAppTheme(isDarkMode: Boolean)
}
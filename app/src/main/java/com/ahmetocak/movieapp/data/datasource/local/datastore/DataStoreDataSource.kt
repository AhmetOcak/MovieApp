package com.ahmetocak.movieapp.data.datasource.local.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreDataSource {

    suspend fun getRememberMe(): Boolean

    suspend fun updateRememberMe(value: Boolean)

    suspend fun getAppTheme(): Flow<Boolean>

    suspend fun updateAppTheme(isDarkMode: Boolean)
}
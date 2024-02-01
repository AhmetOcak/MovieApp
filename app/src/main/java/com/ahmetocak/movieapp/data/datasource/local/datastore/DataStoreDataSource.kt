package com.ahmetocak.movieapp.data.datasource.local.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreDataSource {

    suspend fun getAppTheme(): Flow<Boolean>

    suspend fun updateAppTheme(darkMode: Boolean)

    suspend fun getDynamicColor(): Flow<Boolean>

    suspend fun updateDynamicColor(dynamicColor: Boolean)
}
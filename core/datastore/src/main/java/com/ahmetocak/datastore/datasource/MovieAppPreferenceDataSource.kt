package com.ahmetocak.datastore.datasource

import kotlinx.coroutines.flow.Flow

interface MovieAppPreferenceDataSource {

    suspend fun getAppTheme(): Flow<Boolean>

    suspend fun updateAppTheme(darkMode: Boolean)

    suspend fun getDynamicColor(): Flow<Boolean>

    suspend fun updateDynamicColor(dynamicColor: Boolean)
}
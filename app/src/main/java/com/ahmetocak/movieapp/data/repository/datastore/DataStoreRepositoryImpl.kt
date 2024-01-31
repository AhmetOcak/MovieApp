package com.ahmetocak.movieapp.data.repository.datastore

import com.ahmetocak.movieapp.data.datasource.local.datastore.DataStoreDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStoreDataSource: DataStoreDataSource
) : DataStoreRepository {

    override suspend fun getRememberMe(): Boolean = dataStoreDataSource.getRememberMe()

    override suspend fun updateRememberMe(value: Boolean) =
        dataStoreDataSource.updateRememberMe(value)

    override suspend fun getAppTheme(): Flow<Boolean> = dataStoreDataSource.getAppTheme()

    override suspend fun updateAppTheme(isDarkMode: Boolean) =
        dataStoreDataSource.updateAppTheme(isDarkMode)
}
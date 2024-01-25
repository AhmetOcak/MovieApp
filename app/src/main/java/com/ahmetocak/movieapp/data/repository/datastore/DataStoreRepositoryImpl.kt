package com.ahmetocak.movieapp.data.repository.datastore

import com.ahmetocak.movieapp.data.datasource.local.datastore.DataStoreDataSource
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStoreDataSource: DataStoreDataSource
) : DataStoreRepository {

    override suspend fun getRememberMe(): Boolean = dataStoreDataSource.getRememberMe()

    override suspend fun updateRememberMe(value: Boolean) =
        dataStoreDataSource.updateRememberMe(value)
}
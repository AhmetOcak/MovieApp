package com.ahmetocak.movieapp.data.datasource.local.datastore

interface DataStoreDataSource {

    suspend fun getRememberMe(): Boolean

    suspend fun updateRememberMe(value: Boolean)
}
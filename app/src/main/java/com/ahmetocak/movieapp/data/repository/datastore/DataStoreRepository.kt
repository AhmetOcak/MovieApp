package com.ahmetocak.movieapp.data.repository.datastore

interface DataStoreRepository {

    suspend fun getRememberMe(): Boolean

    suspend fun updateRememberMe(value: Boolean)
}
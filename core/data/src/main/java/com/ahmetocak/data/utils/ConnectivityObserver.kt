package com.ahmetocak.data.utils

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observer(): Flow<Status>

    fun isNetworkAvailable(): Boolean

    enum class Status {
        Available, Unavailable, Lost
    }
}
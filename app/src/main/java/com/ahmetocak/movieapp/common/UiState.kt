package com.ahmetocak.movieapp.common

import com.ahmetocak.movieapp.common.helpers.UiText

sealed interface UiState<out T> {
    data object Loading: UiState<Nothing>
    data class OnDataLoaded<T>(val data: T): UiState<T>
    data class OnError(val errorMessage: UiText): UiState<Nothing>
}
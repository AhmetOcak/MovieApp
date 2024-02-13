package com.ahmetocak.movieapp.common

import androidx.compose.runtime.Stable
import com.ahmetocak.movieapp.common.helpers.UiText

/**
 * Sealed interface representing different UI states with associated data.
 *
 * @param T The type of data associated with the UI state.
 */
@Stable
sealed interface UiState<out T> {
    data object Loading: UiState<Nothing>
    data class OnDataLoaded<T>(val data: T): UiState<T>
    data class OnError(val errorMessage: UiText): UiState<Nothing>
}
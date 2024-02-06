package com.ahmetocak.movieapp.common

/**
 * Sealed interface representing different UI events related to dialogs.
 */
sealed interface DialogUiEvent {
    data object Loading: DialogUiEvent
    data object Active: DialogUiEvent
    data object InActive: DialogUiEvent
}
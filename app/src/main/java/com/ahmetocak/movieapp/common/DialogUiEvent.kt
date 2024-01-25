package com.ahmetocak.movieapp.common

sealed interface DialogUiEvent {
    data object Loading: DialogUiEvent
    data object Active: DialogUiEvent
    data object InActive: DialogUiEvent
}
package com.ahmetocak.movieapp.common

import com.ahmetocak.movieapp.common.helpers.UiText

sealed interface Response<out T> {
    class Success<T>(val data: T): Response<T>
    class Error<T>(val errorMessage: UiText): Response<T>
}
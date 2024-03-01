package com.ahmetocak.common.helpers

/**
 * Sealed interface representing a response, which can be either a success with data or an error.
 *
 * @param T The type of data associated with the response.
 */
sealed interface Response<out T> {
    class Success<T>(val data: T): Response<T>
    class Error<T>(val errorMessage: UiText): Response<T>
}
package com.ahmetocak.movieapp.common

import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.helpers.UiText
import java.io.IOException

/**
 * A generic function for making API calls with error handling.
 *
 * This function is designed to be used with suspending API calls. It wraps the API call
 * with try-catch blocks to handle exceptions and provides a uniform way of dealing with responses.
 *
 * @param call A suspending lambda function representing the API call.
 * @return A sealed class [Response] containing either the success data or an error message.
 */
suspend inline fun <T> apiCall(crossinline call: suspend () -> T): Response<T> {
    return try {
        Response.Success(data = call())
    } catch (e: IOException) {
        Response.Error(errorMessage = UiText.StringResource(R.string.internet_error))
    } catch (e: Exception) {
        Response.Error(errorMessage = e.message?.let { message -> UiText.DynamicString(message) }
            ?: UiText.StringResource(R.string.unknown_error))
    }
}

/**
 * A generic function for making database-related calls with error handling.
 *
 * This function is designed to be used with suspending database operations. It wraps the database
 * operation with a try-catch block to handle exceptions and provides a uniform way of dealing with responses.
 *
 * @param call A suspending lambda function representing the database operation.
 * @return A sealed class [Response] containing either the success data or an error message.
 */
suspend inline fun <T> dbCall(crossinline call: suspend () -> T): Response<T> {
    return try {
        Response.Success(data = call())
    } catch (e: Exception) {
        Response.Error(errorMessage = e.message?.let { message -> UiText.DynamicString(message) }
            ?: UiText.StringResource(R.string.unknown_error))
    }
}
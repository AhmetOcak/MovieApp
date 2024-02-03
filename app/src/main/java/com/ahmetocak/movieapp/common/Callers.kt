package com.ahmetocak.movieapp.common

import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.helpers.UiText
import java.io.IOException

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

suspend inline fun <T> dbCall(crossinline call: suspend () -> T): Response<T> {
    return try {
        Response.Success(data = call())
    } catch (e: Exception) {
        Response.Error(errorMessage = e.message?.let { message -> UiText.DynamicString(message) }
            ?: UiText.StringResource(R.string.unknown_error))
    }
}
package com.ahmetocak.movieapp.common.helpers

import android.util.Log
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.Response
import java.io.IOException

suspend inline fun <T> apiCall(crossinline call: suspend () -> T): Response<T> {
    return try {
        Response.Success(data = call())
    } catch (e: IOException) {
        Log.e("API CALL EXCEPTION", e.stackTraceToString())
        Response.Error(errorMessage = UiText.StringResource(R.string.internet_error))
    } catch (e: Exception) {
        Log.e("API CALL EXCEPTION", e.stackTraceToString())
        Response.Error(errorMessage = e.message?.let { message -> UiText.DynamicString(message) }
            ?: UiText.StringResource(R.string.unknown_error))
    }
}

suspend inline fun <T> dbCall(crossinline call: suspend () -> T): Response<T> {
    return try {
        Response.Success(data = call())
    } catch (e: Exception) {
        Log.e("DB CALL EXCEPTION", e.stackTraceToString())
        Response.Error(errorMessage = e.message?.let { message -> UiText.DynamicString(message) }
            ?: UiText.StringResource(R.string.unknown_error))
    }
}
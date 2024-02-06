package com.ahmetocak.movieapp.utils

import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.helpers.UiText
import com.google.android.gms.tasks.Task

/**
 * Handles the completion of a Firebase Task and invokes appropriate callbacks based on the result.
 *
 * @param taskCall The Firebase Task to be handled.
 * @param onTaskSuccess Callback function to be executed when the task is successful.
 *                      It receives the result of the task as a parameter (may be null).
 * @param onTaskError Callback function to be executed when the task encounters an error.
 */
fun <T> taskHandler(
    taskCall: Task<T>,
    onTaskSuccess: (T?) -> Unit,
    onTaskError: (UiText) -> Unit
) {
    taskCall.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            onTaskSuccess(task.result)
        } else {
            onTaskError(task.exception?.message?.let { message ->
                UiText.DynamicString(message)
            } ?: run {
                UiText.StringResource(R.string.unknown_error)
            })
        }
    }
}

/**
 * Handles an exception from a task operation and returns a [UiText] representation of the error message.
 *
 * @param e The exception to be handled.
 * @param defaultErrorMessage The default [UiText] to be used when the exception does not provide a message.
 * @return A UiText representation of the error message.
 */
fun handleTaskError(
    e: Exception?,
    defaultErrorMessage: UiText = UiText.StringResource(R.string.unknown_error)
): UiText {
    return e?.message?.let { errorMessage ->
        UiText.DynamicString(errorMessage)
    } ?: defaultErrorMessage
}
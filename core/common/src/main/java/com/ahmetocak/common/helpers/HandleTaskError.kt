package com.ahmetocak.common.helpers

import com.ahmetocak.common.R

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
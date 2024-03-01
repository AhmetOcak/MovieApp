package com.ahmetocak.common.helpers

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Sealed class representing different types of UI text resources.
 */
sealed class UiText {
    /**
     * Represents a dynamic string value.
     *
     * @param value The dynamic string value.
     */
    data class DynamicString(val value: String) : UiText()

    /**
     * Represents a string resource with optional format arguments.
     *
     * @param resId The resource ID of the string.
     * @param args Optional format arguments for the string resource.
     */
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ): UiText()

    @Composable
    fun asString(): String {
        return when(this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }

    fun asString(context: Context): String {
        return when(this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, args)
        }
    }
}
package com.ahmetocak.movieapp.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Patterns
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ahmetocak.movieapp.R

@Composable
fun Int.convertToDurationTime(): String =
    "${this / 60}${stringResource(id = R.string.hour_text)} ${this % 60}${stringResource(id = R.string.min_tex)}"

@Composable
fun Float.roundToDecimal(): String = String.format("%.1f", this)

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun String.isValidEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()
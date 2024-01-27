package com.ahmetocak.movieapp.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ahmetocak.movieapp.R

@Composable
fun Int.convertToDurationTime(): String =
    "${this / 60}${stringResource(id = R.string.hour_text)} ${this % 60}${stringResource(id = R.string.min_tex)}"

@Composable
fun Float.roundToDecimal(): String = String.format("%.1f", this)
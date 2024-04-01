package com.ahmetocak.common.helpers

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun isScreenPortrait(): Boolean =
    LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

fun isScreenPortrait(context: Context): Boolean =
    context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
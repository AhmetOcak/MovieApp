package com.ahmetocak.designsystem

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun computeWindowSizeClasses(activity: Activity = LocalContext.current as Activity): WindowWidthSizeClasses? {
    return when (calculateWindowSizeClass(activity = activity).widthSizeClass) {
        WindowWidthSizeClass.Compact -> WindowWidthSizeClasses.COMPACT

        WindowWidthSizeClass.Medium -> WindowWidthSizeClasses.MEDIUM

        WindowWidthSizeClass.Expanded -> WindowWidthSizeClasses.EXPANDED

        else -> null
    }
}

sealed interface WindowWidthSizeClasses {
    data object COMPACT : WindowWidthSizeClasses
    data object MEDIUM : WindowWidthSizeClasses
    data object EXPANDED : WindowWidthSizeClasses
}
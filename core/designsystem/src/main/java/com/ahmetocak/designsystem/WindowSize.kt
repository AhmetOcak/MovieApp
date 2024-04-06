package com.ahmetocak.designsystem

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun computeWindowWidthSize(activity: Activity = LocalContext.current as Activity): WindowSizeClasses? {
    return when (calculateWindowSizeClass(activity = activity).widthSizeClass) {
        WindowWidthSizeClass.Compact -> WindowSizeClasses.COMPACT

        WindowWidthSizeClass.Medium -> WindowSizeClasses.MEDIUM

        WindowWidthSizeClass.Expanded -> WindowSizeClasses.EXPANDED

        else -> null
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun computeWindowHeightSize(activity: Activity = LocalContext.current as Activity): WindowSizeClasses? {
    return when (calculateWindowSizeClass(activity = activity).heightSizeClass) {
        WindowHeightSizeClass.Compact -> WindowSizeClasses.COMPACT

        WindowHeightSizeClass.Medium -> WindowSizeClasses.MEDIUM

        WindowHeightSizeClass.Expanded -> WindowSizeClasses.EXPANDED

        else -> null
    }
}

sealed interface WindowSizeClasses {
    data object COMPACT : WindowSizeClasses
    data object MEDIUM : WindowSizeClasses
    data object EXPANDED : WindowSizeClasses
}
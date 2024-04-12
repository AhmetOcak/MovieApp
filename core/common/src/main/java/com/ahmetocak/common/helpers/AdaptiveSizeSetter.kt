package com.ahmetocak.common.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.ahmetocak.designsystem.WindowSizeClasses
import com.ahmetocak.designsystem.computeWindowHeightSize

@Composable
fun setSize(
    windowSizeClasses: WindowSizeClasses = computeWindowHeightSize() ?: WindowSizeClasses.MEDIUM,
    onCompact: Dp,
    onMedium: Dp,
    onExpanded: Dp
): Dp {
    return when (windowSizeClasses) {
        is WindowSizeClasses.COMPACT -> {
            onCompact
        }

        is WindowSizeClasses.MEDIUM -> {
            onMedium
        }

        is WindowSizeClasses.EXPANDED -> {
            onExpanded
        }
    }
}
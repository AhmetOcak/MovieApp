package com.ahmetocak.designsystem.dimens

import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.WindowSizeClasses
import com.ahmetocak.designsystem.computeWindowWidthSize

fun Modifier.setAdaptiveWidth(
    compactWidthRatio: Int = 1,
    mediumWidthRatio: Int = 2,
    expandedWidthRatio: Int = 2
): Modifier = this.composed {
    computeWindowWidthSize()?.let { windowWidthSizeClasses ->
        when (windowWidthSizeClasses) {
            WindowSizeClasses.COMPACT -> {
                then(Modifier.width(LocalConfiguration.current.screenWidthDp.dp / compactWidthRatio))
            }

            WindowSizeClasses.MEDIUM -> {
                then(Modifier.width(LocalConfiguration.current.screenWidthDp.dp / mediumWidthRatio))
            }

            WindowSizeClasses.EXPANDED -> {
                then(Modifier.width(LocalConfiguration.current.screenWidthDp.dp / expandedWidthRatio))
            }
        }
    } ?: this
}
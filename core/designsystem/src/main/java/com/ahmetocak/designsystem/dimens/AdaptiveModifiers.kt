package com.ahmetocak.designsystem.dimens

import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.WindowWidthSizeClasses
import com.ahmetocak.designsystem.computeWindowSizeClasses

fun Modifier.setAdaptiveWidth(
    compactWidthRatio: Int = 1,
    mediumWidthRation: Int = 2,
    expandedWidthRation: Int = 2
): Modifier = this.composed {
    computeWindowSizeClasses()?.let { windowWidthSizeClasses ->
        when (windowWidthSizeClasses) {
            WindowWidthSizeClasses.COMPACT -> {
                then(Modifier.width(LocalConfiguration.current.screenWidthDp.dp / compactWidthRatio))
            }

            WindowWidthSizeClasses.MEDIUM -> {
                then(Modifier.width(LocalConfiguration.current.screenWidthDp.dp / mediumWidthRation))
            }

            WindowWidthSizeClasses.EXPANDED -> {
                then(Modifier.width(LocalConfiguration.current.screenWidthDp.dp / expandedWidthRation))
            }
        }
    } ?: this
}
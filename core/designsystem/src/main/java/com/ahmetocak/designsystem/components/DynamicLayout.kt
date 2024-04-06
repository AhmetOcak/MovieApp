package com.ahmetocak.designsystem.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration

/**
 * A composable function to dynamically adjust the layout based on screen orientation.
 * In portrait mode, it arranges the content in a column, while in landscape mode, it arranges
 * the content in a row.
 *
 * @param modifier the modifier for the layout
 * @param horizontalAlignment the horizontal alignment of the content
 * @param horizontalArrangement the horizontal arrangement of the content
 * @param verticalAlignment the vertical alignment of the content
 * @param verticalArrangement the vertical arrangement of the content
 * @param content the composable content to be displayed within the layout
 */
@Composable
inline fun DynamicLayout(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(
            modifier = modifier,
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement
        ) {
            content()
        }
    } else {
        Row(
            modifier = modifier,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment
        ) {
            content()
        }
    }
}
package com.ahmetocak.movieapp.presentation.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ahmetocak.movieapp.utils.Dimens

@Composable
fun MovieDialog(
    onDismissRequest: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp - Dimens.fourLevelPadding),
            shape = RoundedCornerShape(28.dp),
            content = content
        )
    }
}
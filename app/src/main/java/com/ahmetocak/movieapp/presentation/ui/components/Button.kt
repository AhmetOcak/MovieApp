package com.ahmetocak.movieapp.presentation.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MovieButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    height: Dp = 56.dp
) {
    Button(modifier = modifier.height(height), onClick = onClick) {
        Text(text = text)
    }
}
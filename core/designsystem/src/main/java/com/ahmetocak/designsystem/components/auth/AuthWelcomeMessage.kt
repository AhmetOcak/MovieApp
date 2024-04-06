package com.ahmetocak.designsystem.components.auth

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ahmetocak.designsystem.dimens.setAdaptiveWidth

@Composable
fun AuthWelcomeMessage(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier.setAdaptiveWidth(),
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.W500),
    )
}
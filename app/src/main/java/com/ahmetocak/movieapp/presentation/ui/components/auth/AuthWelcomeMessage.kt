package com.ahmetocak.movieapp.presentation.ui.components.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AuthWelcomeMessage(text: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.W500),
    )
}
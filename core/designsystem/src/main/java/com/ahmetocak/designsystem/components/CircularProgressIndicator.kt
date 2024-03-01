package com.ahmetocak.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.dimens.ComponentDimens

@Composable
fun ButtonCircularProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(ComponentDimens.buttonCircularProgressIndicatorSize),
        strokeWidth = 2.dp
    )
}

@Composable
fun FullScreenCircularProgressIndicator(paddingValues: PaddingValues = PaddingValues(0.dp)) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
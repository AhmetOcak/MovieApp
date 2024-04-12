package com.ahmetocak.common.helpers

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
inline fun ShowUserMessage(
    message: String,
    toastLength: Int = Toast.LENGTH_LONG,
    consumedMessage: () -> Unit = {}
) {
    Toast.makeText(
        LocalContext.current,
        message,
        toastLength
    ).show()
    consumedMessage()
}
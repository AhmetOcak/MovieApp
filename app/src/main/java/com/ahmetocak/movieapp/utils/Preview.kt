package com.ahmetocak.movieapp.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, showSystemUi = true, device = "id:pixel_6")
@Preview(
    name = "dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES, device = "id:pixel_6"
)
annotation class ScreenPreview

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "dark")
annotation class ComponentPreview
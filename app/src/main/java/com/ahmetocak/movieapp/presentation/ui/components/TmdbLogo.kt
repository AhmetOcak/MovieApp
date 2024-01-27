package com.ahmetocak.movieapp.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.presentation.ui.theme.TmdbBlue

@Composable
fun TmdbLogo(modifier: Modifier = Modifier, logoSize: Dp = 96.dp) {
    Image(
        modifier = modifier
            .size(logoSize)
            .background(TmdbBlue)
            .padding(4.dp),
        painter = painterResource(id = R.drawable.tmdb_logo),
        contentDescription = null
    )
}
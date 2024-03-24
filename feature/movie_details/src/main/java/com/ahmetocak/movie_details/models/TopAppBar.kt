package com.ahmetocak.movie_details.models

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.dimens.Dimens
import com.ahmetocak.designsystem.theme.TransparentWhite
import com.ahmetocak.movie_details.R

@Composable
internal fun TopAppBar(
    upPress: () -> Unit,
    isMovieInWatchList: Boolean,
    onWatchListClick: () -> Unit,
    isWatchlistButtonInProgress: Boolean,
    onGeminiClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.twoLevelPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TransparentIconButton(onClick = upPress) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.Black
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding)) {
            TransparentIconButton(onClick = onGeminiClick) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_google_gemini),
                    contentDescription = null
                )
            }
            TransparentIconButton(onClick = onWatchListClick) {
                if (isWatchlistButtonInProgress) {
                    CircularProgressIndicator()
                } else {
                    Icon(
                        imageVector = if (isMovieInWatchList) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun TransparentIconButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(containerColor = TransparentWhite),
        content = content
    )
}
package com.ahmetocak.movie_details.models

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.components.AnimatedAsyncImage
import com.ahmetocak.designsystem.dimens.Dimens

private val ACTOR_IMAGE_SIZE = 128.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ActorItem(
    actorId: Int,
    imageUrl: String,
    actorName: String,
    characterName: String,
    onClick: (Int) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp / 1.53f),
        onClick = { onClick(actorId) }) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedAsyncImage(
                modifier = Modifier
                    .size(ACTOR_IMAGE_SIZE)
                    .aspectRatio(1f),
                imageUrl = imageUrl
            )
            Column(
                modifier = Modifier.padding(Dimens.oneLevelPadding)
            ) {
                Text(
                    text = actorName,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(Dimens.oneLevelPadding))
                Text(text = characterName, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}
package com.ahmetocak.movie_details.models

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.components.AnimatedAsyncImage
import com.ahmetocak.designsystem.dimens.Dimens

private val ACTOR_IMAGE_SIZE = 128.dp

@Composable
internal fun ActorItem(
    actorId: Int,
    imageUrl: String,
    actorName: String,
    characterName: String,
    onClick: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding)
    ) {
        AnimatedAsyncImage(
            modifier = Modifier
                .size(ACTOR_IMAGE_SIZE)
                .aspectRatio(1f)
                .clip(CircleShape)
                .clickable(onClick = { onClick(actorId) }),
            imageUrl = imageUrl,
            borderShape = CircleShape,
            borderStroke = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            modifier = Modifier.width(ACTOR_IMAGE_SIZE),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(actorName)
                }
                append(" ")
                append("($characterName)")
            },
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}
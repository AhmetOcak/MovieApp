package com.ahmetocak.movie_details.models

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetocak.common.constants.TMDB
import com.ahmetocak.designsystem.components.AnimatedAsyncImage
import com.ahmetocak.movie_details.R
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@Composable
internal fun UserReviewItem(
    author: String,
    authorImagePath: String?,
    content: String,
    createdAt: String,
    updatedAt: String?,
    rating: Int?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (authorImagePath != null) {
                AnimatedAsyncImage(
                    modifier = Modifier
                        .size(24.dp)
                        .aspectRatio(1f),
                    imageUrl = "${TMDB.IMAGE_URL}$authorImagePath"
                )
            }
            Text(
                text = author,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (rating != null) {
                RatingBar(
                    value = rating.toFloat() / 2f,
                    style = RatingBarStyle.Default,
                    onValueChange = {},
                    onRatingChanged = {},
                    size = 12.dp,
                    spaceBetween = 1.dp
                )
            }
            if (updatedAt != null) {
                Text(
                    text = "${createdAt.take(10)} ${stringResource(id = R.string.updated_at)} ${
                        updatedAt.take(10)
                    }",
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Text(text = createdAt.take(10), style = MaterialTheme.typography.bodySmall)
            }
        }
        Text(text = content, style = MaterialTheme.typography.bodyMedium)
    }
}
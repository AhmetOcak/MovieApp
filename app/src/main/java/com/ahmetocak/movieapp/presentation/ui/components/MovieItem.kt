package com.ahmetocak.movieapp.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetocak.movieapp.presentation.ui.theme.RatingStarColor
import com.ahmetocak.movieapp.utils.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    id: Int,
    name: String,
    categories: List<String>,
    imageUrl: String,
    voteAverage: Double,
    voteCount: Int,
    onClick: (Int) -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        onClick = { onClick(id) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
            AnimatedAsyncImage(modifier = Modifier.fillMaxSize(), imageUrl = imageUrl)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.twoLevelPadding),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildString { append(categories.joinToString(", ")) },
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = RatingStarColor
                    )
                    Text(
                        text = "${String.format("%.1f", voteAverage)} ($voteCount)",
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                    )
                }
            }
        }
    }
}
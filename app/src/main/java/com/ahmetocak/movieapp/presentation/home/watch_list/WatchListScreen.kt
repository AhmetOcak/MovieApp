package com.ahmetocak.movieapp.presentation.home.watch_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.presentation.ui.components.AnimatedAsyncImage
import com.ahmetocak.movieapp.presentation.ui.components.MovieScaffold
import com.ahmetocak.movieapp.presentation.ui.theme.TransparentWhite
import com.ahmetocak.movieapp.utils.Dimens
import com.ahmetocak.movieapp.utils.TMDB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchListScreen(modifier: Modifier = Modifier) {

    MovieScaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.my_watch_list_text))
                }
            )
        }
    ) { paddingValues ->
        WatchListScreenContent(modifier = Modifier.padding(paddingValues))
    }
}

@Composable
private fun WatchListScreenContent(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.twoLevelPadding)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = Dimens.twoLevelPadding),
            horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
        ) {
            items(3) {
                WatchListItem(
                    imageUrl = "${TMDB.IMAGE_URL}/cwJrBL09kZAl7P2DQUttkPa6rob.jpg",
                    movieName = "Napolean",
                    releaseYear = "2023",
                    categories = listOf("Dram", "War", "Love"),
                    voteAverage = 9.3,
                    voteCount = 3241,
                    movieId = 1,
                    onClick = {},
                    onRemoveFromWatchListClick = {}
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WatchListItem(
    imageUrl: String,
    movieName: String,
    releaseYear: String,
    categories: List<String>,
    voteAverage: Double,
    voteCount: Int,
    movieId: Int,
    onClick: (Int) -> Unit,
    onRemoveFromWatchListClick: (Int) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        onClick = { onClick(movieId) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding)) {
            Box(
                modifier = Modifier
                    .weight(5f)
                    .fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                AnimatedAsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    imageUrl = imageUrl,
                    contentScale = ContentScale.FillBounds
                )
                IconButton(
                    onClick = { onRemoveFromWatchListClick(movieId) },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = TransparentWhite)
                ) {
                    Icon(imageVector = Icons.Filled.BookmarkRemove, contentDescription = null)
                }
            }
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxSize()
                    .padding(horizontal = Dimens.oneLevelPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding)
            ) {
                Text(text = movieName)
                Text(
                    text = "$releaseYear ${buildString { append(categories.joinToString(", ")) }}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color.Yellow
                    )
                    Text(text = "$voteAverage ($voteCount)")
                }
            }
        }
    }
}
package com.ahmetocak.movieapp.presentation.home.watch_list

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.domain.mapper.toWatchListMovie
import com.ahmetocak.movieapp.model.firebase.firestore.WatchListMovie
import com.ahmetocak.movieapp.model.watch_list.WatchListEntity
import com.ahmetocak.movieapp.presentation.components.designsystem.AnimatedAsyncImage
import com.ahmetocak.movieapp.presentation.components.designsystem.FullScreenCircularProgressIndicator
import com.ahmetocak.movieapp.presentation.components.designsystem.MovieScaffold
import com.ahmetocak.movieapp.presentation.home.HomeSections
import com.ahmetocak.movieapp.presentation.home.MovieNavigationBar
import com.ahmetocak.movieapp.presentation.theme.RatingStarColor
import com.ahmetocak.movieapp.presentation.theme.TransparentWhite
import com.ahmetocak.movieapp.utils.ComponentDimens
import com.ahmetocak.movieapp.utils.Dimens
import com.ahmetocak.movieapp.utils.TMDB
import com.ahmetocak.movieapp.utils.roundToDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchListScreen(
    modifier: Modifier = Modifier,
    onMovieClick: (Int) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    viewModel: WatchListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.userMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.userMessages.first().asString(),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.onUserMessageConsumed()
    }

    MovieScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.my_watch_list_text))
                }
            )
        },
        bottomBar = {
            MovieNavigationBar(
                tabs = HomeSections.entries.toTypedArray(),
                currentRoute = HomeSections.WATCH_LIST.route,
                navigateToRoute = onNavigateToRoute
            )
        }
    ) { paddingValues ->
        WatchListScreenContent(
            modifier = Modifier.padding(paddingValues),
            onMovieClick = onMovieClick,
            watchList = uiState.watchList,
            isLoading = uiState.isLoading,
            onRemoveFromWatchListClick = remember(viewModel) { viewModel::deleteMovieFromWatchList }
        )
    }
}

@Composable
private fun WatchListScreenContent(
    modifier: Modifier,
    onMovieClick: (Int) -> Unit,
    watchList: List<WatchListEntity>,
    isLoading: Boolean,
    onRemoveFromWatchListClick: (WatchListMovie) -> Unit
) {
    if (isLoading) {
        FullScreenCircularProgressIndicator()
    } else {
        if (watchList.isNotEmpty()) {
            Column(
                modifier = modifier.fillMaxSize()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(Dimens.twoLevelPadding),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
                    verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
                ) {
                    items(watchList, key = { item -> item.movieId }) { movie ->
                        WatchListItem(
                            imageUrl = "${TMDB.IMAGE_URL}${movie.imageUrlPath}",
                            movieName = movie.movieName,
                            releaseYear = movie.releaseYear,
                            voteAverage = movie.voteAverage,
                            voteCount = movie.voteCount,
                            movieId = movie.movieId,
                            onClick = onMovieClick,
                            onRemoveFromWatchListClick = {
                                onRemoveFromWatchListClick(movie.toWatchListMovie())
                            }
                        )
                    }
                }
            }
        } else {
            EmptyWatchListView(modifier = modifier)
        }
    }
}

@Composable
private fun EmptyWatchListView(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.fourLevelPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(ComponentDimens.emptyWarningImageSize),
            painter = painterResource(id = R.drawable.empty),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(top = Dimens.twoLevelPadding),
            text = stringResource(id = R.string.watch_list_empty),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WatchListItem(
    imageUrl: String,
    movieName: String,
    releaseYear: String,
    voteAverage: Float,
    voteCount: Int,
    movieId: Int,
    onClick: (Int) -> Unit,
    onRemoveFromWatchListClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick(movieId) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding)) {
            Box(
                modifier = Modifier.aspectRatio(2f / 3f),
                contentAlignment = Alignment.TopEnd
            ) {
                AnimatedAsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    imageUrl = imageUrl
                )
                IconButton(
                    onClick = onRemoveFromWatchListClick,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = TransparentWhite)
                ) {
                    Icon(
                        imageVector = Icons.Filled.BookmarkRemove,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.oneLevelPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding)
            ) {
                Text(
                    text = movieName,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = releaseYear,
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
                        tint = RatingStarColor
                    )
                    Text(
                        text = "${voteAverage.roundToDecimal()} ($voteCount)",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
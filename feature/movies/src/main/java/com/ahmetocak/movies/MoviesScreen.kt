package com.ahmetocak.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.common.constants.SeeAllType
import com.ahmetocak.common.constants.TMDB
import com.ahmetocak.common.helpers.conditional
import com.ahmetocak.common.helpers.isScreenPortrait
import com.ahmetocak.designsystem.components.ErrorView
import com.ahmetocak.designsystem.components.FullScreenCircularProgressIndicator
import com.ahmetocak.designsystem.components.MovieButton
import com.ahmetocak.designsystem.components.MovieScaffold
import com.ahmetocak.designsystem.components.navigation.MovieNavigationBar
import com.ahmetocak.designsystem.dimens.Dimens
import com.ahmetocak.navigation.HomeSections
import com.ahmetocak.ui.MovieItem

@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (SeeAllType) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    showNavigationRail: Boolean,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MovieScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (!showNavigationRail) {
                MovieNavigationBar(
                    tabs = HomeSections.entries.toTypedArray(),
                    currentRoute = HomeSections.MOVIES.route,
                    navigateToRoute = onNavigateToRoute
                )
            }
        }
    ) { paddingValues ->
        MoviesScreenContent(
            modifier = Modifier
                .padding(paddingValues)
                .padding(start = if (showNavigationRail) 80.dp else 0.dp),
            onMovieClick = onMovieClick,
            onSeeAllClick = onSeeAllClick,
            nowPlayingMoviesState = uiState.nowPlayingMoviesState,
            popularMoviesState = uiState.popularMoviesState,
            isScreenPortrait = isScreenPortrait()
        )
    }
}

@Composable
private fun MoviesScreenContent(
    modifier: Modifier,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (SeeAllType) -> Unit,
    nowPlayingMoviesState: MovieState,
    popularMoviesState: MovieState,
    isScreenPortrait: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .conditional(
                condition = !isScreenPortrait,
                ifTrue = { verticalScroll(rememberScrollState()) }
            )
            .padding(vertical = Dimens.twoLevelPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        MovieSection(
            modifier = Modifier.conditional(
                condition = isScreenPortrait,
                ifTrue = { weight(1f).padding(horizontal = Dimens.oneLevelPadding) },
                ifFalse = { height(LocalConfiguration.current.screenHeightDp.dp) }
            ),
            movieItemModifier = Modifier
                .conditional(
                    condition = isScreenPortrait,
                    ifTrue = { aspectRatio(4f / 3f).padding(horizontal = Dimens.oneLevelPadding) },
                    ifFalse = { aspectRatio(2f / 3f) }
                ),
            listContentPadding = PaddingValues(horizontal = if (isScreenPortrait) 0.dp else Dimens.twoLevelPadding),
            horizontalArrangement = Arrangement.spacedBy(if (isScreenPortrait) 0.dp else Dimens.twoLevelPadding),
            onSeeAllClick = onSeeAllClick,
            onMovieClick = onMovieClick,
            movieState = nowPlayingMoviesState,
            seeAllType = SeeAllType.NOW_PLAYING,
            title = stringResource(id = R.string.now_playing),
            usePosterImage = !isScreenPortrait
        )
        MovieSection(
            modifier = Modifier.conditional(
                condition = isScreenPortrait,
                ifTrue = { weight(1f) },
                ifFalse = { height(LocalConfiguration.current.screenHeightDp.dp) }
            ),
            movieItemModifier = Modifier.aspectRatio(2f / 3f),
            listContentPadding = PaddingValues(horizontal = Dimens.twoLevelPadding),
            horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
            onSeeAllClick = onSeeAllClick,
            onMovieClick = onMovieClick,
            movieState = popularMoviesState,
            seeAllType = SeeAllType.POPULAR,
            title = stringResource(id = R.string.popular_movies),
            usePosterImage = true
        )
    }
}

@Composable
private fun MovieSection(
    modifier: Modifier = Modifier,
    movieItemModifier: Modifier = Modifier,
    listContentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal = if (!reverseLayout) Arrangement.Start else Arrangement.End,
    onSeeAllClick: (SeeAllType) -> Unit,
    onMovieClick: (Int) -> Unit,
    movieState: MovieState,
    seeAllType: SeeAllType,
    title: String,
    usePosterImage: Boolean
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        ContentTitleSection(
            text = title,
            onSeeAllClick = onSeeAllClick,
            type = seeAllType
        )
        when (movieState) {
            is MovieState.Loading -> {
                FullScreenCircularProgressIndicator()
            }

            is MovieState.OnDataLoaded -> {
                LazyRow(
                    contentPadding = listContentPadding,
                    horizontalArrangement = horizontalArrangement
                ) {
                    items(movieState.movieList, key = { movie -> movie.id }) { movie ->
                        MovieItem(
                            modifier = movieItemModifier,
                            id = movie.id,
                            name = movie.movieName,
                            releaseDate = movie.releaseDate,
                            imageUrl = "${TMDB.IMAGE_URL}${if (usePosterImage) movie.posterImagePath else movie.backdropImagePath}",
                            voteAverage = movie.voteAverage,
                            voteCount = movie.voteCount ?: 0,
                            onClick = onMovieClick
                        )
                    }
                }
            }

            is MovieState.OnError -> {
                ErrorView(
                    modifier = Modifier.fillMaxSize(),
                    errorMessage = movieState.errorMessage.asString()
                )
            }
        }
    }
}


@Composable
private fun ContentTitleSection(
    text: String,
    onSeeAllClick: (SeeAllType) -> Unit,
    type: SeeAllType
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.twoLevelPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        MovieButton(
            text = stringResource(id = R.string.see_all_text),
            onClick = { onSeeAllClick(type) },
            fontSize = 16.sp
        )
    }
}
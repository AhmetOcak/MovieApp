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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.common.constants.SeeAllType
import com.ahmetocak.common.constants.TMDB
import com.ahmetocak.common.helpers.isScreenPortrait
import com.ahmetocak.designsystem.WindowSizeClasses
import com.ahmetocak.designsystem.components.ErrorView
import com.ahmetocak.designsystem.components.FullScreenCircularProgressIndicator
import com.ahmetocak.designsystem.components.MovieButton
import com.ahmetocak.designsystem.components.MovieScaffold
import com.ahmetocak.designsystem.components.navigation.MovieNavigationBar
import com.ahmetocak.designsystem.dimens.Dimens
import com.ahmetocak.navigation.HomeSections
import com.ahmetocak.ui.MovieItem
import com.ahmetocak.common.R as CommonR

@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (SeeAllType) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    showNavigationRail: Boolean,
    windowHeightSizeClass: WindowSizeClasses,
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
            trendingMoviesState = uiState.trendingMoviesState,
            topRatedMoviesState = uiState.topRatedMoviesState,
            upcomingMoviesState = uiState.upcomingMoviesState,
            topMovieSectionImageSize = setSize(
                windowHeightSizeClass = windowHeightSizeClass,
                onCompact = LocalConfiguration.current.screenHeightDp.dp - Dimens.twoLevelPadding,
                onMedium = if (isScreenPortrait()) LocalConfiguration.current.screenHeightDp.dp / 2
                else LocalConfiguration.current.screenHeightDp.dp / 1.75f,
                onExpanded = LocalConfiguration.current.screenHeightDp.dp / 1.5f
            ),
            movieSectionImageSize = setSize(
                windowHeightSizeClass = windowHeightSizeClass,
                onCompact = LocalConfiguration.current.screenHeightDp.dp - Dimens.twoLevelPadding,
                onMedium = if (isScreenPortrait()) LocalConfiguration.current.screenHeightDp.dp / 2.5f
                else LocalConfiguration.current.screenHeightDp.dp / 2,
                onExpanded = LocalConfiguration.current.screenHeightDp.dp / 1.75f
            )
        )
    }
}

@Composable
private fun MoviesScreenContent(
    modifier: Modifier,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (SeeAllType) -> Unit,
    trendingMoviesState: MovieState,
    topRatedMoviesState: MovieState,
    upcomingMoviesState: MovieState,
    topMovieSectionImageSize: Dp,
    movieSectionImageSize: Dp
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = Dimens.twoLevelPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        MovieSection(
            sectionHeight = topMovieSectionImageSize,
            onSeeAllClick = onSeeAllClick,
            onMovieClick = onMovieClick,
            movieState = trendingMoviesState,
            seeAllType = SeeAllType.TRENDING,
            title = stringResource(id = CommonR.string.trending)
        )
        MovieSection(
            sectionHeight = movieSectionImageSize,
            onSeeAllClick = onSeeAllClick,
            onMovieClick = onMovieClick,
            movieState = topRatedMoviesState,
            seeAllType = SeeAllType.TOP_RATED,
            title = stringResource(id = CommonR.string.top_rated)
        )
        MovieSection(
            sectionHeight = movieSectionImageSize,
            onSeeAllClick = onSeeAllClick,
            onMovieClick = onMovieClick,
            movieState = upcomingMoviesState,
            seeAllType = SeeAllType.UPCOMING,
            title = stringResource(id = CommonR.string.upcoming)
        )
    }
}

@Composable
private fun MovieSection(
    sectionHeight: Dp,
    movieImageRatio: Float = 2f / 3f,
    onSeeAllClick: (SeeAllType) -> Unit,
    onMovieClick: (Int) -> Unit,
    movieState: MovieState,
    seeAllType: SeeAllType,
    title: String
) {
    Column(
        modifier = Modifier.height(sectionHeight),
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
                    contentPadding = PaddingValues(horizontal = Dimens.twoLevelPadding),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
                ) {
                    items(movieState.movieList, key = { movie -> movie.id }) { movie ->
                        MovieItem(
                            modifier = Modifier.aspectRatio(movieImageRatio),
                            id = movie.id,
                            name = movie.movieName,
                            releaseDate = movie.releaseDate,
                            imageUrl = "${TMDB.IMAGE_URL}${movie.posterImagePath}",
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

private fun setSize(
    windowHeightSizeClass: WindowSizeClasses,
    onCompact: Dp,
    onMedium: Dp,
    onExpanded: Dp
): Dp {
    return when (windowHeightSizeClass) {
        is WindowSizeClasses.COMPACT -> {
            onCompact
        }

        is WindowSizeClasses.MEDIUM -> {
            onMedium
        }

        is WindowSizeClasses.EXPANDED -> {
            onExpanded
        }
    }
}
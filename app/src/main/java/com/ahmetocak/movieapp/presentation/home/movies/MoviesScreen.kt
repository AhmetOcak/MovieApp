package com.ahmetocak.movieapp.presentation.home.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.presentation.home.HomeSections
import com.ahmetocak.movieapp.presentation.home.MovieNavigationBar
import com.ahmetocak.movieapp.presentation.ui.components.ErrorView
import com.ahmetocak.movieapp.presentation.ui.components.FullScreenCircularProgressIndicator
import com.ahmetocak.movieapp.presentation.ui.components.MovieButton
import com.ahmetocak.movieapp.presentation.ui.components.MovieItem
import com.ahmetocak.movieapp.presentation.ui.components.MovieScaffold
import com.ahmetocak.movieapp.presentation.ui.theme.MovieAppTheme
import com.ahmetocak.movieapp.utils.Dimens
import com.ahmetocak.movieapp.utils.ScreenPreview
import com.ahmetocak.movieapp.utils.SeeAllType
import com.ahmetocak.movieapp.utils.TMDB

private val POPULAR_MOVIE_ITEM_WIDTH = 196.dp

@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (SeeAllType) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    MovieScaffold(
        modifier = modifier,
        bottomBar = {
            MovieNavigationBar(
                tabs = HomeSections.entries.toTypedArray(),
                currentRoute = HomeSections.MOVIES.route,
                navigateToRoute = onNavigateToRoute
            )
        }
    ) { paddingValues ->
        MoviesScreenContent(
            modifier = Modifier.padding(paddingValues),
            onMovieClick = onMovieClick,
            onSeeAllClick = onSeeAllClick,
            nowPlayingMoviesState = uiState.nowPlayingMoviesState,
            popularMoviesState = uiState.popularMoviesState
        )
    }
}

@Composable
private fun MoviesScreenContent(
    modifier: Modifier,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (SeeAllType) -> Unit,
    nowPlayingMoviesState: MovieState,
    popularMoviesState: MovieState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = Dimens.twoLevelPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        MovieSection(
            modifier = Modifier.weight(1f),
            movieItemModifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp)
                .fillMaxHeight()
                .padding(horizontal = Dimens.twoLevelPadding),
            onSeeAllClick = onSeeAllClick,
            onMovieClick = onMovieClick,
            movieState = nowPlayingMoviesState,
            seeAllType = SeeAllType.UPCOMING,
            title = stringResource(id = R.string.now_playing_text)
        )
        MovieSection(
            modifier = Modifier.weight(1f),
            movieItemModifier = Modifier.width(POPULAR_MOVIE_ITEM_WIDTH),
            listContentPadding = PaddingValues(horizontal = Dimens.twoLevelPadding),
            horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
            onSeeAllClick = onSeeAllClick,
            onMovieClick = onMovieClick,
            movieState = popularMoviesState,
            seeAllType = SeeAllType.POPULAR,
            title = stringResource(id = R.string.see_all_text)
        )
    }
}

@Composable
private fun MovieSection(
    modifier: Modifier = Modifier,
    movieItemModifier: Modifier = Modifier,
    listContentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal =
        if (!reverseLayout) Arrangement.Start else Arrangement.End,
    onSeeAllClick: (SeeAllType) -> Unit,
    onMovieClick: (Int) -> Unit,
    movieState: MovieState,
    seeAllType: SeeAllType,
    title: String
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
                    items(movieState.movieList, key = { it.id }) { movie ->
                        MovieItem(
                            modifier = movieItemModifier,
                            id = movie.id,
                            name = movie.movieName ?: "*",
                            categories = movie.genreIds.map { it.toString() },
                            imageUrl = "${TMDB.IMAGE_URL}${movie.movieImageUrlPath}",
                            voteAverage = movie.voteAverage ?: 0.0,
                            voteCount = movie.voteCount ?: 0,
                            onClick = onMovieClick
                        )
                    }
                }
            }

            is MovieState.OnError -> {
                ErrorView(
                    modifier = Modifier.fillMaxSize(),
                    errorMessage = movieState.errorMessage?.asString()
                        ?: stringResource(id = R.string.unknown_error)
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

@ScreenPreview
@Composable
private fun MoviesScreenPreview() {
    MovieAppTheme {
        Surface {
            MoviesScreen(onMovieClick = {}, onSeeAllClick = {}, onNavigateToRoute = {})
        }
    }
}
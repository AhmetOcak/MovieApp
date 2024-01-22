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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.presentation.home.HomeSections
import com.ahmetocak.movieapp.presentation.home.MovieNavigationBar
import com.ahmetocak.movieapp.presentation.ui.components.MovieItem
import com.ahmetocak.movieapp.presentation.ui.components.MovieScaffold
import com.ahmetocak.movieapp.presentation.ui.theme.MovieAppTheme
import com.ahmetocak.movieapp.utils.Dimens
import com.ahmetocak.movieapp.utils.ScreenPreview
import com.ahmetocak.movieapp.utils.SeeAllType
import com.ahmetocak.movieapp.utils.TMDB

@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (SeeAllType) -> Unit,
    onNavigateToRoute: (String) -> Unit
) {

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
            onSeeAllClick = onSeeAllClick
        )
    }
}

@Composable
private fun MoviesScreenContent(
    modifier: Modifier,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (SeeAllType) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = Dimens.twoLevelPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        NowPlayingMoviesSection(
            modifier = Modifier.weight(1f),
            onSeeAllClick = onSeeAllClick,
            onMovieClick = onMovieClick
        )
        PopularMoviesSection(
            modifier = Modifier.weight(1f),
            onSeeAllClick = onSeeAllClick,
            onMovieClick = onMovieClick
        )
    }
}

@Composable
private fun PopularMoviesSection(
    modifier: Modifier,
    onSeeAllClick: (SeeAllType) -> Unit,
    onMovieClick: (Int) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        ContentTitleSection(
            text = stringResource(id = R.string.popular_movies_text),
            onSeeAllClick = onSeeAllClick,
            type = SeeAllType.POPULAR
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = Dimens.twoLevelPadding),
            horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
        ) {
            items(6) {
                MovieItem(
                    id = 0,
                    name = "The Movie Name",
                    categories = listOf("Drama", "Fear", "Sport"),
                    imageUrl = "${TMDB.IMAGE_URL}/cwJrBL09kZAl7P2DQUttkPa6rob.jpg",
                    voteAverage = 8.7,
                    voteCount = 213,
                    onClick = onMovieClick
                )
            }
        }
    }
}

@Composable
private fun NowPlayingMoviesSection(
    modifier: Modifier,
    onSeeAllClick: (SeeAllType) -> Unit,
    onMovieClick: (Int) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        ContentTitleSection(
            text = stringResource(id = R.string.now_playing_text),
            onSeeAllClick = onSeeAllClick,
            type = SeeAllType.UPCOMING
        )
        LazyRow {
            items(5) {
                MovieItem(
                    modifier = Modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp)
                        .fillMaxHeight()
                        .padding(horizontal = Dimens.twoLevelPadding),
                    id = 0,
                    name = "The Movie Name",
                    categories = listOf("Drama", "Fear", "Sport"),
                    imageUrl = "${TMDB.IMAGE_URL}/f1AQhx6ZfGhPZFTVKgxG91PhEYc.jpg",
                    voteAverage = 8.7,
                    voteCount = 123,
                    onClick = onMovieClick
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
        Button(onClick = { onSeeAllClick(type) }) {
            Text(text = stringResource(id = R.string.see_all_text))
        }
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
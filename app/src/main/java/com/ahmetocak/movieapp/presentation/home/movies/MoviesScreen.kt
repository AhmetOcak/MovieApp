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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.presentation.ui.components.MovieItem
import com.ahmetocak.movieapp.presentation.ui.components.MovieScaffold
import com.ahmetocak.movieapp.presentation.ui.theme.MovieAppTheme
import com.ahmetocak.movieapp.utils.ScreenPreview
import com.ahmetocak.movieapp.utils.TMDB

@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    onMovieSelected: (Int) -> Unit,
    onSeeAllSelected: () -> Unit
) {

    MovieScaffold(modifier = modifier) { paddingValues ->
        MoviesScreenContent(modifier = Modifier.padding(paddingValues))
    }
}

@Composable
private fun MoviesScreenContent(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = dimensionResource(id = R.dimen.two_level_padding)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_padding))
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_padding))
        ) {
            ContentTitleSection(
                text = stringResource(id = R.string.now_playing_text),
                onSeeAllClicked = {}
            )
            LazyRow {
                items(5) {
                    MovieItem(
                        modifier = Modifier
                            .width(LocalConfiguration.current.screenWidthDp.dp)
                            .fillMaxHeight()
                            .padding(horizontal = dimensionResource(id = R.dimen.two_level_padding)),
                        id = 0,
                        name = "The Movie Name",
                        categories = listOf("Drama", "Fear", "Sport"),
                        imageUrl = "${TMDB.IMAGE_URL}/f1AQhx6ZfGhPZFTVKgxG91PhEYc.jpg",
                        voteAverage = 8.7,
                        voteCount = 123,
                        onClick = {}
                    )
                }
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_padding))
        ) {
            ContentTitleSection(
                text = stringResource(id = R.string.popular_movies_text),
                onSeeAllClicked = {}
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = dimensionResource(id = R.dimen.two_level_padding)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_padding))
            ) {
                items(6) {
                    MovieItem(
                        id = 0,
                        name = "The Movie Name",
                        categories = listOf("Drama", "Fear", "Sport"),
                        imageUrl = "${TMDB.IMAGE_URL}/cwJrBL09kZAl7P2DQUttkPa6rob.jpg",
                        voteAverage = 8.7,
                        voteCount = 213,
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun ContentTitleSection(text: String, onSeeAllClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.two_level_padding)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Button(onClick = onSeeAllClicked) {
            Text(text = stringResource(id = R.string.see_all_text))
        }
    }
}

@ScreenPreview
@Composable
private fun MoviesScreenPreview() {
    MovieAppTheme {
        Surface {
            MoviesScreen(onMovieSelected = {}, onSeeAllSelected = {})
        }
    }
}
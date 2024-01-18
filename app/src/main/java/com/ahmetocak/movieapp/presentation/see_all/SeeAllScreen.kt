package com.ahmetocak.movieapp.presentation.see_all

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.presentation.ui.components.MovieItem
import com.ahmetocak.movieapp.presentation.ui.components.MovieScaffold
import com.ahmetocak.movieapp.utils.ScreenPreview
import com.ahmetocak.movieapp.utils.TMDB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeeAllScreen(modifier: Modifier = Modifier, upPress: () -> Unit) {

    MovieScaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Now Playing")
                },
                navigationIcon = {
                    IconButton(onClick = upPress) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        SeeAllScreenContent(modifier = Modifier.padding(paddingValues))
    }
}

@Composable
private fun SeeAllScreenContent(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.two_level_padding))
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.two_level_padding)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_padding))
        ) {
            items(8) {
                MovieItem(
                    id = 0,
                    name = "The Movie Name",
                    categories = listOf("Love", "Drama", "War", "Sport"),
                    imageUrl = "${TMDB.IMAGE_URL}/cwJrBL09kZAl7P2DQUttkPa6rob.jpg",
                    voteAverage = 8.7,
                    voteCount = 423,
                    onClick = { }
                )
            }
        }
    }
}

@ScreenPreview
@Composable
private fun SeeAllScreenPreview() {
    SeeAllScreen(upPress = {})
}
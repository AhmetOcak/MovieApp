package com.ahmetocak.movieapp.presentation.movie_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.presentation.ui.components.AnimatedAsyncImage
import com.ahmetocak.movieapp.presentation.ui.components.MovieScaffold
import com.ahmetocak.movieapp.utils.TMDB
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@Composable
fun MovieDetailsScreen(modifier: Modifier = Modifier, upPress: () -> Unit) {

    MovieScaffold(modifier = modifier) { paddingValues ->
        MovieDetailsScreenContent(
            modifier = Modifier.padding(paddingValues),
            movieImageUrl = "${TMDB.IMAGE_URL}/cwJrBL09kZAl7P2DQUttkPa6rob.jpg",
            upPress = upPress,
            movieName = "Napolean",
            voteAverage = 8.7.toFloat(),
            voteCount = 324,
            categories = listOf("Dram", "War", "Love"),
            releaseDate = "2023-11-22",
            movieOverview = "An epic that details the checkered rise and fall of French Emperor Napoleon Bonaparte and his relentless journey to power through the prism of his addictive, volatile relationship with his wife, Josephine.",
            movieDuration = "${158 / 60}${stringResource(id = R.string.hour_text)} ${158 % 60}${
                stringResource(id = R.string.min_tex)}",
            directing = "Ridley Scott"
        )
    }
}

@Composable
private fun MovieDetailsScreenContent(
    modifier: Modifier,
    movieImageUrl: String,
    upPress: () -> Unit,
    movieName: String,
    voteAverage: Float,
    voteCount: Int,
    categories: List<String>,
    releaseDate: String,
    movieOverview: String,
    movieDuration: String,
    directing: String
) {
    val movieImageHeight: Dp =
        (LocalConfiguration.current.screenHeightDp.dp / 2) + LocalConfiguration.current.screenHeightDp.dp / 8

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = dimensionResource(id = R.dimen.two_level_padding))
    ) {
        Box {
            AnimatedAsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(movieImageHeight),
                imageUrl = movieImageUrl
            )
            TopAppBar(upPress = upPress)
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.two_level_padding)))
        Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_padding))) {
            Text(
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.two_level_padding)),
                text = movieName
            )
            MovieDetails(
                voteAverage = voteAverage,
                voteCount = voteCount,
                categories = categories,
                releaseDate = releaseDate,
                movieDuration = movieDuration,
                directing = directing
            )
            Text(
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.two_level_padding)),
                text = movieOverview
            )
            ActorList()
        }
    }
}

@Composable
private fun ActorList() {
    Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_padding))) {
        Text(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.two_level_padding)),
            text = stringResource(id = R.string.actors_text)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = dimensionResource(id = R.dimen.two_level_padding)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_padding))
        ) {
            items(5) {
                ActorItem(
                    imageUrl = "${TMDB.IMAGE_URL}/dD7hrRueEZmQgGWBp7pAOOt5gLV.jpg",
                    actorName = "Joaquin Phoenix",
                    characterName = "Napoleon Bonaparte"
                )
            }
        }
    }
}

@Composable
private fun MovieDetails(
    voteAverage: Float,
    voteCount: Int,
    categories: List<String>,
    releaseDate: String,
    movieDuration: String,
    directing: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.two_level_padding)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_padding))) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_padding)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RatingBar(value = voteAverage / 2f,
                    style = RatingBarStyle.Default,
                    onValueChange = {},
                    onRatingChanged = {},
                    size = 14.dp,
                    spaceBetween = 1.dp
                )
                Text(text = "$voteAverage ($voteCount ${stringResource(id = R.string.review_text)})")
            }
            Text(text = buildString { append(categories.joinToString(", ")) })
            Text(text = releaseDate)
            Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_padding))) {
                Icon(imageVector = Icons.Outlined.AccessTime, contentDescription = null)
                Text(text = movieDuration)
            }
            Text(text = "Directing $directing")
        }
        Image(
            modifier = Modifier
                .size(96.dp)
                .background(Color(0xFF0d253f))
                .padding(4.dp),
            painter = painterResource(id = R.drawable.tmdb_logo),
            contentDescription = null
        )
    }
}

@Composable
private fun ActorItem(imageUrl: String, actorName: String, characterName: String) {
    ElevatedCard(modifier = Modifier.height(96.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            AnimatedAsyncImage(
                modifier = Modifier.fillMaxHeight(), imageUrl = imageUrl
            )
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.one_level_padding)),
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(id = R.dimen.one_level_padding)
                )
            ) {
                Text(text = actorName, fontWeight = FontWeight.Bold)
                Text(text = characterName)
            }
        }
    }
}

@Composable
private fun TopAppBar(upPress: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.two_level_padding)),
        horizontalArrangement = Arrangement.Start
    ) {
        Button(
            modifier = Modifier.size(36.dp),
            shape = CircleShape,
            onClick = upPress,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.7f)),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack, contentDescription = null, tint = Color.Black
            )
        }
    }
}
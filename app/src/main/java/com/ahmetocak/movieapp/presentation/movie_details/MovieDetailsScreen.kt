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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.presentation.ui.components.AnimatedAsyncImage
import com.ahmetocak.movieapp.presentation.ui.components.MovieScaffold
import com.ahmetocak.movieapp.presentation.ui.theme.TransparentWhite
import com.ahmetocak.movieapp.utils.Dimens
import com.ahmetocak.movieapp.utils.TMDB
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

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
                stringResource(id = R.string.min_tex)
            }",
            directing = "Ridley Scott",
            isMovieInWatchList = false,
            onWatchListClick = {}
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
    directing: String,
    isMovieInWatchList: Boolean,
    onWatchListClick: () -> Unit
) {
    val movieImageHeight: Dp =
        (LocalConfiguration.current.screenHeightDp.dp / 2) + LocalConfiguration.current.screenHeightDp.dp / 8

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Box {
            AnimatedAsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(movieImageHeight),
                imageUrl = movieImageUrl
            )
            TopAppBar(
                upPress = upPress,
                isMovieInWatchList = isMovieInWatchList,
                onWatchListClick = onWatchListClick
            )
        }
        Spacer(modifier = Modifier.height(Dimens.twoLevelPadding))
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding)) {
            Text(
                modifier = Modifier.padding(horizontal = Dimens.twoLevelPadding),
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
                modifier = Modifier.padding(horizontal = Dimens.twoLevelPadding),
                text = movieOverview
            )
            ActorList()
        }
        TrailerList()
    }
}

@Composable
private fun ActorList() {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)) {
        Text(
            modifier = Modifier.padding(horizontal = Dimens.twoLevelPadding),
            text = stringResource(id = R.string.actors_text)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = Dimens.twoLevelPadding),
            horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
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
private fun TrailerList() {
    LazyColumn(
        modifier = Modifier.height(512.dp),
        contentPadding = PaddingValues(Dimens.twoLevelPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        item {
            Text(text = stringResource(id = R.string.trailers_text))
        }
        items(2) {
            TrailerItem(videoId = "8H2qXJz3mdM", "Napolyon | TR Altyazılı Fragman | 24 Kasım 2023")
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
            .padding(horizontal = Dimens.twoLevelPadding),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RatingBar(
                    value = voteAverage / 2f,
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
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding)) {
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
                modifier = Modifier.padding(Dimens.oneLevelPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding)
            ) {
                Text(text = actorName, fontWeight = FontWeight.Bold)
                Text(text = characterName)
            }
        }
    }
}

@Composable
private fun TrailerItem(videoId: String, title: String) {
    ElevatedCard(
        modifier = Modifier
            .width(LocalConfiguration.current.screenWidthDp.dp)
            .height(256.dp)
    ) {
        Column {
            AndroidView(
                factory = {
                    val view = YouTubePlayerView(it)
                    view.addYouTubePlayerListener(
                        object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                super.onReady(youTubePlayer)
                                youTubePlayer.cueVideo(videoId, 0f)
                            }
                        }
                    )
                    view
                }
            )
            Text(
                modifier = Modifier.padding(Dimens.oneLevelPadding),
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TopAppBar(
    upPress: () -> Unit,
    isMovieInWatchList: Boolean,
    onWatchListClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.twoLevelPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = upPress,
            colors = IconButtonDefaults.iconButtonColors(containerColor = TransparentWhite)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null
            )
        }
        IconButton(
            onClick = onWatchListClick,
            colors = IconButtonDefaults.iconButtonColors(containerColor = TransparentWhite)
        ) {
            Icon(
                imageVector = if (isMovieInWatchList) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                contentDescription = null
            )
        }
    }
}
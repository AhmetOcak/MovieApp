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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.presentation.ui.components.AnimatedAsyncImage
import com.ahmetocak.movieapp.presentation.ui.components.MovieScaffold
import com.ahmetocak.movieapp.presentation.ui.theme.TmdbBlue
import com.ahmetocak.movieapp.presentation.ui.theme.TransparentWhite
import com.ahmetocak.movieapp.presentation.ui.theme.errorDark
import com.ahmetocak.movieapp.utils.Dimens
import com.ahmetocak.movieapp.utils.TMDB
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

private val TRAILER_LIST_HEIGHT = 512.dp
private val TMDB_LOGO_SIZE = 96.dp
private val ACTOR_ITEM_HEIGHT = 128.dp

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
            director = "Ridley Scott",
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
    director: String,
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
                text = movieName,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            MovieDetails(
                voteAverage = voteAverage,
                voteCount = voteCount,
                categories = categories,
                releaseDate = releaseDate,
                movieDuration = movieDuration,
                director = director
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
            text = stringResource(id = R.string.actors_text),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
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
        modifier = Modifier.height(TRAILER_LIST_HEIGHT),
        contentPadding = PaddingValues(Dimens.twoLevelPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        item {
            Text(
                text = stringResource(id = R.string.trailers_text),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
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
    director: String
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
                Icon(
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Text(text = movieDuration, color = MaterialTheme.colorScheme.error)
            }
            Text(text = buildAnnotatedString {
                append("${stringResource(id = R.string.director_text)}: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(director)
                }
            })
        }
        Image(
            modifier = Modifier
                .size(TMDB_LOGO_SIZE)
                .background(TmdbBlue)
                .padding(4.dp),
            painter = painterResource(id = R.drawable.tmdb_logo),
            contentDescription = null
        )
    }
}

@Composable
private fun ActorItem(imageUrl: String, actorName: String, characterName: String) {
    ElevatedCard(modifier = Modifier.height(ACTOR_ITEM_HEIGHT)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            AnimatedAsyncImage(
                modifier = Modifier.fillMaxHeight(), imageUrl = imageUrl
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.oneLevelPadding),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = actorName, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(Dimens.oneLevelPadding))
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
                contentDescription = null,
                tint = Color.Black
            )
        }
        IconButton(
            onClick = onWatchListClick,
            colors = IconButtonDefaults.iconButtonColors(containerColor = TransparentWhite)
        ) {
            Icon(
                imageVector = if (isMovieInWatchList) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}
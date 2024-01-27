package com.ahmetocak.movieapp.presentation.movie_details

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.UiState
import com.ahmetocak.movieapp.domain.model.MovieCredit
import com.ahmetocak.movieapp.domain.model.MovieDetail
import com.ahmetocak.movieapp.model.movie_detail.MovieTrailer
import com.ahmetocak.movieapp.presentation.ui.components.AnimatedAsyncImage
import com.ahmetocak.movieapp.presentation.ui.components.ErrorView
import com.ahmetocak.movieapp.presentation.ui.components.FullScreenCircularProgressIndicator
import com.ahmetocak.movieapp.presentation.ui.components.MovieScaffold
import com.ahmetocak.movieapp.presentation.ui.components.TmdbLogo
import com.ahmetocak.movieapp.presentation.ui.theme.TransparentWhite
import com.ahmetocak.movieapp.utils.Dimens
import com.ahmetocak.movieapp.utils.TMDB
import com.ahmetocak.movieapp.utils.convertToDurationTime
import com.ahmetocak.movieapp.utils.roundToDecimal
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

private val TRAILER_LIST_HEIGHT = 512.dp
private val ACTOR_ITEM_HEIGHT = 128.dp

private val errorModifier = Modifier
    .fillMaxWidth()
    .padding(vertical = Dimens.fourLevelPadding)

private val circularProgressIndicatorPadding = PaddingValues(Dimens.fourLevelPadding)

@Composable
fun MovieDetailsScreen(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    MovieScaffold(modifier = modifier) { paddingValues ->
        MovieDetailsScreenContent(
            modifier = Modifier.padding(paddingValues),
            upPress = upPress,
            isMovieInWatchList = false,
            onWatchListClick = {},
            detailUiState = uiState.detailUiState,
            directorName = uiState.directorName,
            castUiState = uiState.castUiState,
            trailerUiState = uiState.trailersUiState
        )
    }
}

@Composable
private fun MovieDetailsScreenContent(
    modifier: Modifier,
    upPress: () -> Unit,
    isMovieInWatchList: Boolean,
    onWatchListClick: () -> Unit,
    detailUiState: UiState<MovieDetail>,
    directorName: String,
    castUiState: UiState<MovieCredit>,
    trailerUiState: UiState<MovieTrailer>
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        MovieSection(
            upPress = upPress,
            isMovieInWatchList = isMovieInWatchList,
            onWatchListClick = onWatchListClick,
            detailUiState = detailUiState,
            directorName = directorName
        )
        ActorListSection(castUiState = castUiState)
        TrailerListSection(trailerUiState = trailerUiState)
    }
}

@Composable
private fun MovieSection(
    upPress: () -> Unit,
    isMovieInWatchList: Boolean,
    onWatchListClick: () -> Unit,
    detailUiState: UiState<MovieDetail>,
    directorName: String
) {
    val movieImageHeight: Dp =
        (LocalConfiguration.current.screenHeightDp.dp / 2) + LocalConfiguration.current.screenHeightDp.dp / 8

    when (detailUiState) {
        is UiState.Loading -> {
            FullScreenCircularProgressIndicator(paddingValues = circularProgressIndicatorPadding)
        }

        is UiState.OnDataLoaded -> {
            detailUiState.data.apply {
                Box {
                    AnimatedAsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(movieImageHeight),
                        imageUrl = "${TMDB.IMAGE_URL}${imageUrlPath}"
                    )
                    TopAppBar(
                        upPress = upPress,
                        isMovieInWatchList = isMovieInWatchList,
                        onWatchListClick = onWatchListClick
                    )
                }
                MovieDetails(
                    voteAverage = voteAverage,
                    voteCount = voteCount,
                    categories = genres.joinToString(),
                    releaseDate = releaseDate,
                    movieDuration = duration.convertToDurationTime(),
                    directorName = directorName,
                    movieName = movieName,
                    overview = overview
                )
            }
        }

        is UiState.OnError -> {
            ErrorView(
                modifier = errorModifier,
                errorMessage = detailUiState.errorMessage.asString()
            )
        }
    }
}

@Composable
private fun MovieDetails(
    voteAverage: Float,
    voteCount: Int,
    categories: String,
    releaseDate: String,
    movieDuration: String,
    directorName: String,
    movieName: String,
    overview: String
) {
    Column(
        modifier = Modifier.padding(horizontal = Dimens.twoLevelPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(end = Dimens.twoLevelPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
            ) {
                Text(
                    text = movieName,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding),
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
                    Text(text = "${voteAverage.roundToDecimal()} ($voteCount ${stringResource(id = R.string.review_text)})")
                }
                Text(text = categories)
                Text(text = releaseDate)
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding)) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = movieDuration,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Text(text = buildAnnotatedString {
                    append("${stringResource(id = R.string.director_text)}: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(directorName)
                    }
                })
            }
            TmdbLogo(modifier = Modifier.weight(1f))
        }
        Text(text = overview)
    }
}

@Composable
private fun ActorListSection(castUiState: UiState<MovieCredit>) {
    when (castUiState) {
        is UiState.Loading -> {
            FullScreenCircularProgressIndicator(paddingValues = circularProgressIndicatorPadding)
        }

        is UiState.OnDataLoaded -> {
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
                    items(castUiState.data.cast, key = { it.id }) { cast ->
                        ActorItem(
                            imageUrl = "${TMDB.IMAGE_URL}${cast.imageUrlPath}",
                            actorName = cast.name,
                            characterName = cast.characterName
                        )
                    }
                }
            }
        }

        is UiState.OnError -> {
            ErrorView(
                modifier = errorModifier,
                errorMessage = castUiState.errorMessage.asString()
            )
        }
    }
}

@Composable
private fun TrailerListSection(trailerUiState: UiState<MovieTrailer>) {
    when (trailerUiState) {
        is UiState.Loading -> {
            FullScreenCircularProgressIndicator(paddingValues = circularProgressIndicatorPadding)
        }

        is UiState.OnDataLoaded -> {
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
                items(trailerUiState.data.trailers) { trailer ->
                    TrailerItem(
                        videoId = trailer.key,
                        title = trailer.name
                    )
                }
            }
        }

        is UiState.OnError -> {
            ErrorView(
                modifier = errorModifier,
                errorMessage = trailerUiState.errorMessage.asString()
            )
        }
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
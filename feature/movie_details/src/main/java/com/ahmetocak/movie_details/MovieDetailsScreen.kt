package com.ahmetocak.movie_details

import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ahmetocak.common.constants.TMDB
import com.ahmetocak.common.convertToDurationTime
import com.ahmetocak.common.helpers.ShowUserMessage
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.common.helpers.conditional
import com.ahmetocak.common.helpers.isScreenPortrait
import com.ahmetocak.designsystem.setSize
import com.ahmetocak.common.roundToDecimal
import com.ahmetocak.designsystem.components.AnimatedAsyncImage
import com.ahmetocak.designsystem.components.ErrorView
import com.ahmetocak.designsystem.components.FullScreenCircularProgressIndicator
import com.ahmetocak.designsystem.components.GeminiLoading
import com.ahmetocak.designsystem.components.MovieScaffold
import com.ahmetocak.designsystem.components.onLoadStateAppend
import com.ahmetocak.designsystem.components.onLoadStateRefresh
import com.ahmetocak.designsystem.dimens.Dimens
import com.ahmetocak.model.firebase.WatchListMovie
import com.ahmetocak.model.movie.RecommendedMovieContent
import com.ahmetocak.model.movie.UserReviewResults
import com.ahmetocak.model.movie_detail.Cast
import com.ahmetocak.model.movie_detail.MovieDetail
import com.ahmetocak.model.movie_detail.Trailer
import com.ahmetocak.movie_details.models.ActorItem
import com.ahmetocak.movie_details.models.TopAppBar
import com.ahmetocak.movie_details.models.TrailerItem
import com.ahmetocak.movie_details.models.UserReviewItem
import com.ahmetocak.ui.MovieItem
import com.ahmetocak.ui.TmdbLogo
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@Composable
fun MovieDetailsScreen(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    onActorClick: (Int) -> Unit,
    onMovieClick: (Int) -> Unit,
    isScreenWidthExpanded: Boolean,
    isScreenHeightCompact: Boolean,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val userReviews = uiState.userReviews.collectAsLazyPagingItems()
    val recommendations = uiState.movieRecommendations.collectAsLazyPagingItems()

    var showBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (uiState.userMessages.isNotEmpty()) {
        ShowUserMessage(
            message = uiState.userMessages.first().asString(),
            consumedMessage = viewModel::consumedUserMessage
        )
    }

    MovieScaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        when (val status = uiState.movieDataStatus) {
            is MovieDataStatus.Loading -> {
                FullScreenCircularProgressIndicator(paddingValues = paddingValues)
            }

            is MovieDataStatus.Error -> {
                ErrorView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    errorMessage = status.message.asString()
                )
            }

            is MovieDataStatus.Success -> {
                MovieDetailsScreenContent(
                    modifier = Modifier.padding(paddingValues),
                    upPress = upPress,
                    isMovieInWatchList = uiState.isMovieInWatchList,
                    onWatchListClick = remember { viewModel::handleWatchListAction },
                    movieDetail = uiState.movieDetails.first(),
                    castList = uiState.movieCast,
                    trailers = uiState.movieTrailers,
                    directorName = uiState.directorName,
                    isWatchlistButtonInProgress = uiState.isWatchlistButtonInProgress,
                    onActorClick = onActorClick,
                    reviews = userReviews,
                    recommendations = recommendations,
                    onMovieClick = onMovieClick,
                    onGeminiClick = remember {
                        {
                            showBottomSheet = true
                            viewModel.getGeminiResponse(context = context)
                        }
                    },
                    onImageLoaded = remember {
                        {
                            if (!isScreenHeightCompact) {
                                viewModel.generatePaletteFromImage(it.toBitmap())
                            }
                        }
                    },
                    posterBackgroundColors = if (isScreenHeightCompact) listOf(
                        Color.Transparent,
                        Color.Transparent
                    ) else uiState.posterBackgroundColors,
                    isScreenHeightCompact = isScreenHeightCompact,
                    isScreenWidthExpanded = isScreenWidthExpanded
                )
            }
        }

        if (showBottomSheet) {
            GeminiSection(
                onDismissRequest = { showBottomSheet = false },
                isLoading = uiState.gemini.isLoading,
                response = uiState.gemini.responseString,
                errorMessage = uiState.gemini.errorMessage
            )
        }
    }
}

@Composable
private fun MovieDetailsScreenContent(
    modifier: Modifier,
    upPress: () -> Unit,
    isMovieInWatchList: Boolean,
    onWatchListClick: (WatchListMovie) -> Unit,
    movieDetail: MovieDetail,
    directorName: String,
    castList: List<Cast>,
    trailers: List<Trailer>,
    isWatchlistButtonInProgress: Boolean,
    onActorClick: (Int) -> Unit,
    reviews: LazyPagingItems<UserReviewResults>,
    recommendations: LazyPagingItems<RecommendedMovieContent>,
    onMovieClick: (Int) -> Unit,
    onGeminiClick: () -> Unit,
    isScreenWidthExpanded: Boolean,
    posterBackgroundColors: List<Color>,
    onImageLoaded: (Drawable) -> Unit,
    isScreenHeightCompact: Boolean
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        MovieSection(
            upPress = upPress,
            isMovieInWatchList = isMovieInWatchList,
            onWatchListClick = onWatchListClick,
            movieDetail = movieDetail,
            directorName = directorName,
            isWatchlistButtonInProgress = isWatchlistButtonInProgress,
            onGeminiClick = onGeminiClick,
            isScreenWidthExpanded = isScreenWidthExpanded,
            posterBackgroundColors = posterBackgroundColors,
            onImageLoaded = onImageLoaded,
            isScreenHeightCompact = isScreenHeightCompact
        )
        if (reviews.itemCount != 0) {
            UserReviewsSection(reviews = reviews, isScreenHeightCompact = isScreenHeightCompact)
        }
        ActorListSection(castList = castList, onActorClick = onActorClick)
        TrailerListSection(trailers = trailers)
        if (recommendations.itemCount != 0) {
            MovieRecommendationsSection(
                recommendations = recommendations,
                onMovieClick = onMovieClick
            )
        }
        Spacer(modifier = Modifier.height(Dimens.twoLevelPadding))
    }
}

@Composable
private fun MovieSection(
    upPress: () -> Unit,
    movieDetail: MovieDetail,
    isMovieInWatchList: Boolean,
    onWatchListClick: (WatchListMovie) -> Unit,
    directorName: String,
    isWatchlistButtonInProgress: Boolean,
    onGeminiClick: () -> Unit,
    isScreenWidthExpanded: Boolean,
    posterBackgroundColors: List<Color>,
    onImageLoaded: (Drawable) -> Unit,
    isScreenHeightCompact: Boolean
) {
    movieDetail.apply {
        Box(
            modifier = Modifier.drawBehind {
                drawRect(brush = Brush.verticalGradient(colors = posterBackgroundColors))
            }
        ) {
            if (!isScreenHeightCompact) {
                AnimatedAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalConfiguration.current.screenHeightDp.dp / 1.33f)
                        .aspectRatio(2f / 3f, true),
                    imageUrl = "${TMDB.IMAGE_URL}${posterImageUrlPath}",
                    onImagePainterSuccess = onImageLoaded
                )
            }
            TopAppBar(
                upPress = upPress,
                isMovieInWatchList = isMovieInWatchList,
                onWatchListClick = {
                    onWatchListClick(
                        WatchListMovie(
                            id = id,
                            name = originalMovieName,
                            releaseYear = releaseDate,
                            voteAverage = voteAverage,
                            voteCount = voteCount,
                            imageUrlPath = posterImageUrlPath
                        )
                    )
                },
                isWatchlistButtonInProgress = isWatchlistButtonInProgress,
                onGeminiClick = onGeminiClick
            )
        }
        MovieDetails(
            voteAverage = voteAverage,
            voteCount = voteCount,
            categories = genres,
            releaseDate = releaseDate,
            movieDuration = duration.convertToDurationTime(),
            directorName = directorName,
            movieName = movieName,
            overview = overview,
            movieOriginalName = originalMovieName,
            isScreenWidthExpanded = isScreenWidthExpanded
        )
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
    overview: String,
    movieOriginalName: String,
    isScreenWidthExpanded: Boolean
) {
    Column(
        modifier = Modifier.padding(horizontal = Dimens.twoLevelPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                modifier = Modifier
                    .conditional(condition = !isScreenWidthExpanded, ifTrue = { weight(3f) })
                    .padding(end = Dimens.twoLevelPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
            ) {
                Text(
                    text = if (movieName == movieOriginalName) movieName else "$movieName ($movieOriginalName)",
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
            TmdbLogo(
                modifier = Modifier.conditional(
                    condition = !isScreenWidthExpanded,
                    ifTrue = { weight(1f).aspectRatio(1f) }
                )
            )
        }
        Text(text = overview)
    }
}

@Composable
private fun ActorListSection(castList: List<Cast>, onActorClick: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)) {
        ContentSubTitle(titleId = R.string.actors_text)
        LazyRow(
            contentPadding = PaddingValues(horizontal = Dimens.twoLevelPadding),
            horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
        ) {
            items(castList, key = { it.id }) { cast ->
                ActorItem(
                    imageUrl = "${TMDB.IMAGE_URL}${cast.imageUrlPath}",
                    actorName = cast.name,
                    characterName = cast.characterName,
                    actorId = cast.id,
                    onClick = onActorClick
                )
            }
        }
    }
}

@Composable
private fun TrailerListSection(trailers: List<Trailer>) {
    if (trailers.isNotEmpty()) {
        ContentSubTitle(titleId = R.string.trailers_text)
        LazyRow(
            contentPadding = PaddingValues(horizontal = Dimens.twoLevelPadding),
            horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
        ) {
            items(trailers, key = { it.key }) { trailer ->
                TrailerItem(videoId = trailer.key)
            }
        }
    }
}

@Composable
private fun UserReviewsSection(
    reviews: LazyPagingItems<UserReviewResults>,
    isScreenHeightCompact: Boolean
) {
    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .conditional(
                    condition = isScreenHeightCompact,
                    ifTrue = { height(LocalConfiguration.current.screenHeightDp.dp / 1.33f) },
                    ifFalse = { height(LocalConfiguration.current.screenHeightDp.dp / 2) }
                ),
            verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
            contentPadding = PaddingValues(vertical = Dimens.oneLevelPadding)
        ) {
            contentStickyHeader(
                titleId = R.string.user_reviews,
                modifier = Modifier.padding(horizontal = Dimens.twoLevelPadding)
            )
            items(reviews.itemCount, key = { it }) { index ->
                reviews[index]?.let { review ->
                    UserReviewItem(
                        author = review.author,
                        authorImagePath = review.authorDetails.avatarPath,
                        content = review.content,
                        createdAt = review.createdAt,
                        updatedAt = review.updatedAt,
                        rating = review.authorDetails.rating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.twoLevelPadding),
                    )
                }
            }

            reviews.loadState.apply {
                onLoadStateRefresh(loadState = refresh)
                onLoadStateAppend(loadState = append, isResultEmpty = reviews.itemCount == 0)
            }
        }
        Divider(
            modifier = Modifier.padding(horizontal = Dimens.twoLevelPadding),
            thickness = 2.dp
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MovieRecommendationsSection(
    recommendations: LazyPagingItems<RecommendedMovieContent>,
    onMovieClick: (Int) -> Unit
) {
    val state = rememberLazyListState()

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    ContentSubTitle(titleId = R.string.recommendations_text)
    LazyRow(
        modifier = Modifier.height(
            setSize(
                onCompact = screenHeight / 1.5f,
                onMedium = if (isScreenPortrait()) screenHeight / 2.5f else screenHeight / 1.75f,
                onExpanded = screenHeight / 2.5f
            )
        ),
        contentPadding = PaddingValues(horizontal = Dimens.twoLevelPadding),
        horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
        state = state,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = state)
    ) {
        items(recommendations.itemCount, key = { it }) { index ->
            recommendations[index]?.let { movie ->
                MovieItem(
                    id = movie.id,
                    name = movie.movieName,
                    releaseDate = movie.releaseDate,
                    imageUrl = "${TMDB.IMAGE_URL}${movie.image}",
                    voteAverage = movie.voteAverage,
                    voteCount = movie.voteCount,
                    onClick = { onMovieClick(movie.id) },
                    modifier = Modifier.aspectRatio(2f / 3f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GeminiSection(
    onDismissRequest: () -> Unit,
    isLoading: Boolean,
    response: String?,
    errorMessage: UiText?
) {
    val parts = response?.split("**")

    val annotatedResponse = buildAnnotatedString {
        parts?.onEachIndexed { index, part ->
            if (index % 2 == 0) {
                append(part)
            } else {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(part)
                }
            }
        }
    }

    ModalBottomSheet(
        modifier = Modifier.defaultMinSize(minHeight = LocalConfiguration.current.screenHeightDp.dp / 2),
        onDismissRequest = onDismissRequest
    ) {
        if (isLoading) {
            AnimatedGeminiLoading()
        } else {
            Text(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(Dimens.twoLevelPadding),
                text = annotatedResponse
            )
        }

        if (errorMessage != null) {
            Toast.makeText(
                LocalContext.current,
                errorMessage.asString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
private fun ContentSubTitle(titleId: Int) {
    Text(
        modifier = Modifier.padding(horizontal = Dimens.twoLevelPadding),
        text = stringResource(id = titleId),
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
    )
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.contentStickyHeader(titleId: Int, modifier: Modifier = Modifier) {
    stickyHeader {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            text = stringResource(id = titleId),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
private fun AnimatedGeminiLoading() {
    Column(modifier = Modifier.padding(Dimens.twoLevelPadding)) {
        GeminiLoading(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        GeminiLoading(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        GeminiLoading(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        GeminiLoading(
            modifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp / 3)
                .height(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
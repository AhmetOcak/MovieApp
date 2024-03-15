package com.ahmetocak.actor_details

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.common.constants.TMDB
import com.ahmetocak.common.helpers.UiState
import com.ahmetocak.designsystem.components.AnimatedAsyncImage
import com.ahmetocak.designsystem.components.ErrorView
import com.ahmetocak.designsystem.components.FullScreenCircularProgressIndicator
import com.ahmetocak.designsystem.components.MovieScaffold
import com.ahmetocak.designsystem.dimens.Dimens
import com.ahmetocak.model.movie.ActorDetails
import com.ahmetocak.model.movie.ActorMoviesContent
import com.ahmetocak.ui.MovieItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActorDetailsScreen(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    onMovieClick: (Int) -> Unit,
    viewModel: ActorDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MovieScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = uiState.actorName)
                },
                navigationIcon = {
                    IconButton(onClick = upPress) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        ActorDetailsScreenContent(
            modifier = Modifier.padding(paddingValues),
            actorDetailsState = uiState.actorDetailsState,
            actorMoviesState = uiState.actorMoviesState,
            onMovieClick = onMovieClick
        )
    }
}

@Composable
private fun ActorDetailsScreenContent(
    modifier: Modifier,
    actorDetailsState: UiState<ActorDetails>,
    actorMoviesState: UiState<List<ActorMoviesContent>>,
    onMovieClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = Dimens.twoLevelPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        ActorDetailsSection(actorDetailsState = actorDetailsState)
        ActorMoviesSection(
            actorMoviesState = actorMoviesState,
            onMovieClick = onMovieClick
        )
    }
}

@Composable
private fun ActorDetailsSection(actorDetailsState: UiState<ActorDetails>) {
    when (actorDetailsState) {
        is UiState.Loading -> {
            FullScreenCircularProgressIndicator()
        }

        is UiState.OnDataLoaded -> {
            actorDetailsState.data.apply {
                AnimatedAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f / 3f),
                    imageUrl = "${TMDB.IMAGE_URL}${profileImagePath}"
                )
                ContentTextRow(subtitleId = R.string.place_of_birth, contentText = placeOfBirth)
                if (deathDay != null) {
                    ContentTextRow(
                        subtitleId = R.string.birth_and_death_day,
                        contentText = "$birthday / $deathDay"
                    )
                } else {
                    ContentTextRow(subtitleId = R.string.birthday, contentText = birthday)
                }
                if (biography.isNotBlank()) {
                    ContentTextRow(subtitleId = R.string.biography, contentText = biography)
                }
            }
        }

        is UiState.OnError -> {
            ErrorView(
                modifier = Modifier.fillMaxWidth(),
                errorMessage = actorDetailsState.errorMessage.toString()
            )
        }
    }
}

@Composable
private fun ActorMoviesSection(
    actorMoviesState: UiState<List<ActorMoviesContent>>,
    onMovieClick: (Int) -> Unit,
) {
    when (actorMoviesState) {
        is UiState.Loading -> {
            FullScreenCircularProgressIndicator()
        }

        is UiState.OnDataLoaded -> {
            Text(
                modifier = Modifier.padding(
                    top = Dimens.oneLevelPadding,
                    start = Dimens.twoLevelPadding
                ),
                text = stringResource(id = R.string.actor_movies),
                fontWeight = FontWeight.Bold
            )
            LazyRow(
                modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp / 3),
                contentPadding = PaddingValues(horizontal = Dimens.twoLevelPadding),
                horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
            ) {
                items(actorMoviesState.data, key = { movie -> movie.id }) { movie ->
                    MovieItem(
                        id = movie.id,
                        name = movie.movieName,
                        releaseDate = movie.releaseDate,
                        imageUrl = "${TMDB.IMAGE_URL}/${movie.posterImagePath}",
                        voteAverage = movie.voteAverage,
                        voteCount = movie.voteCount,
                        onClick = onMovieClick,
                        modifier = Modifier.aspectRatio(2f / 3f)
                    )
                }
            }
        }

        is UiState.OnError -> {
            ErrorView(
                modifier = Modifier.fillMaxWidth(),
                errorMessage = actorMoviesState.errorMessage.toString()
            )
        }
    }
}

@Composable
private fun ContentTextRow(subtitleId: Int, contentText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.twoLevelPadding)
    ) {
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("${stringResource(id = subtitleId)}: ")
            }
            append(contentText)
        })
    }
}
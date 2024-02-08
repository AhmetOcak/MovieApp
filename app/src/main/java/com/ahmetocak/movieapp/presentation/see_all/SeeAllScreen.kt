package com.ahmetocak.movieapp.presentation.see_all

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ahmetocak.movieapp.model.movie.MovieContent
import com.ahmetocak.movieapp.presentation.components.ui.MovieItem
import com.ahmetocak.movieapp.presentation.components.designsystem.MovieScaffold
import com.ahmetocak.movieapp.utils.Dimens
import com.ahmetocak.movieapp.utils.TMDB
import com.ahmetocak.movieapp.utils.onLoadStateAppend
import com.ahmetocak.movieapp.utils.onLoadStateRefresh

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeeAllScreen(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    onMovieClick: (Int) -> Unit,
    viewModel: SeeAllViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val movieList = uiState.movieList.collectAsLazyPagingItems()

    MovieScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = uiState.topBarTitle.asString())
                },
                navigationIcon = {
                    IconButton(onClick = upPress) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        SeeAllScreenContent(
            modifier = Modifier.padding(paddingValues),
            onMovieClick = onMovieClick,
            movieList = movieList
        )
    }
}

@Composable
private fun SeeAllScreenContent(
    modifier: Modifier,
    onMovieClick: (Int) -> Unit,
    movieList: LazyPagingItems<MovieContent>?
) {
    if (movieList != null) {
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.twoLevelPadding),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = Dimens.twoLevelPadding),
            horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
        ) {
            items(movieList.itemCount) { index ->
                movieList[index]?.let { movie ->
                    MovieItem(
                        id = movie.id,
                        name = movie.movieName ?: "",
                        releaseDate = movie.releaseDate ?: "",
                        imageUrl = "${TMDB.IMAGE_URL}${movie.posterImagePath}",
                        voteAverage = movie.voteAverage ?: 0.0,
                        voteCount = movie.voteCount ?: 0,
                        onClick = onMovieClick,
                        modifier = Modifier.aspectRatio(2f / 3f)
                    )
                }
            }

            movieList.loadState.apply {
                onLoadStateRefresh(loadState = refresh)
                onLoadStateAppend(loadState = append, isResultEmpty = movieList.itemCount == 0)
            }
        }
    }
}
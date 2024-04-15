package com.ahmetocak.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ahmetocak.common.constants.TMDB
import com.ahmetocak.common.helpers.isScreenPortrait
import com.ahmetocak.designsystem.WindowSizeClasses
import com.ahmetocak.designsystem.components.MovieScaffold
import com.ahmetocak.designsystem.components.navigation.MovieNavigationBar
import com.ahmetocak.designsystem.components.onLoadStateAppend
import com.ahmetocak.designsystem.components.onLoadStateRefresh
import com.ahmetocak.designsystem.dimens.ComponentDimens
import com.ahmetocak.designsystem.dimens.Dimens
import com.ahmetocak.model.movie.MovieContent
import com.ahmetocak.navigation.HomeSections
import com.ahmetocak.ui.MovieItem

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onNavigateToRoute: (String) -> Unit,
    onMovieClick: (Int) -> Unit,
    showNavigationRail: Boolean,
    windowWidthSizeClass: WindowSizeClasses,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val searchResult = uiState.searchResult.collectAsLazyPagingItems()

    MovieScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (!showNavigationRail) {
                MovieNavigationBar(
                    tabs = HomeSections.entries.toTypedArray(),
                    currentRoute = HomeSections.SEARCH.route,
                    navigateToRoute = onNavigateToRoute
                )
            }
        }
    ) { paddingValues ->
        SearchScreenContent(
            modifier = Modifier
                .padding(paddingValues)
                .padding(start = if (showNavigationRail) 80.dp else 0.dp),
            searchValue = viewModel.query,
            onSearchValueChange = remember { viewModel::updateQueryValue },
            searchError = uiState.queryFieldErrorMessage != null,
            searchLabelText = uiState.queryFieldErrorMessage?.asString()
                ?: stringResource(id = R.string.search_label_text),
            onSearchClick = remember { viewModel::searchMovie },
            searchResult = searchResult,
            onMovieItemClick = onMovieClick,
            isSearchDone = uiState.isSearchDone,
            isScreenWidthCompact = windowWidthSizeClass == WindowSizeClasses.COMPACT,
            isScreenPortrait = isScreenPortrait()
        )
    }
}

@Composable
private fun SearchScreenContent(
    modifier: Modifier,
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    searchError: Boolean,
    searchLabelText: String,
    onSearchClick: () -> Unit,
    searchResult: LazyPagingItems<MovieContent>,
    onMovieItemClick: (Int) -> Unit,
    isSearchDone: Boolean,
    isScreenWidthCompact: Boolean,
    isScreenPortrait: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.twoLevelPadding)
            .padding(top = Dimens.twoLevelPadding)
    ) {
        SearchField(
            value = searchValue,
            onValueChange = onSearchValueChange,
            isError = searchError,
            labelText = searchLabelText,
            onSearchClick = onSearchClick
        )
        if (isSearchDone) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (isScreenWidthCompact) 2 else 3),
                contentPadding = PaddingValues(vertical = Dimens.twoLevelPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
                horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
            ) {
                items(searchResult.itemCount) { index ->
                    searchResult[index]?.let { movie ->
                        MovieItem(
                            id = movie.id,
                            name = movie.movieName,
                            releaseDate = movie.releaseDate,
                            imageUrl = "${TMDB.IMAGE_URL}${movie.posterImagePath}",
                            voteAverage = movie.voteAverage,
                            voteCount = movie.voteCount ?: 0,
                            onClick = onMovieItemClick,
                            modifier = Modifier.aspectRatio(if (isScreenPortrait) 2f / 3f else 1f)
                        )
                    }
                }

                searchResult.loadState.apply {
                    onLoadStateRefresh(loadState = refresh)
                    onLoadStateAppend(
                        loadState = append,
                        isResultEmpty = searchResult.itemCount == 0
                    )
                }
            }
        } else {
            SearchSomethingView()
        }
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    labelText: String,
    onSearchClick: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        label = {
            Text(text = labelText)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = { onSearchClick() }),
        singleLine = true
    )
}

@Composable
private fun SearchSomethingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(ComponentDimens.searchSomethingViewSize),
            imageVector = Icons.Filled.Search,
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.twoLevelPadding)
                .padding(horizontal = Dimens.fourLevelPadding),
            text = stringResource(id = R.string.search_something_text),
            textAlign = TextAlign.Center
        )
    }
}
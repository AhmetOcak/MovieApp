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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ahmetocak.common.constants.TMDB
import com.ahmetocak.common.utils.onLoadStateAppend
import com.ahmetocak.common.utils.onLoadStateRefresh
import com.ahmetocak.designsystem.components.MovieNavigationBar
import com.ahmetocak.designsystem.components.MovieScaffold
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
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val searchResult = uiState.searchResult.collectAsLazyPagingItems()

    MovieScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MovieNavigationBar(
                tabs = HomeSections.entries.toTypedArray(),
                currentRoute = HomeSections.SEARCH.route,
                navigateToRoute = onNavigateToRoute
            )
        }
    ) { paddingValues ->
        SearchScreenContent(
            modifier = Modifier.padding(paddingValues),
            searchValue = viewModel.query,
            onSearchValueChange = remember { viewModel::updateQueryValue },
            searchError = uiState.queryFieldErrorMessage != null,
            searchLabelText = uiState.queryFieldErrorMessage?.asString()
                ?: stringResource(id = R.string.search_label_text),
            onSearchClick = remember { viewModel::searchMovie },
            searchResult = searchResult,
            onMovieItemClick = onMovieClick,
            isSearchDone = uiState.isSearchDone
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
    isSearchDone: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.twoLevelPadding)
            .padding(top = Dimens.twoLevelPadding)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchValue,
            onValueChange = onSearchValueChange,
            isError = searchError,
            label = {
                Text(text = searchLabelText)
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
        if (isSearchDone) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
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
                            modifier = Modifier.aspectRatio(2f / 3f)
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
package com.ahmetocak.movieapp.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.paging.LoadState
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.presentation.components.designsystem.ErrorView
import com.ahmetocak.movieapp.presentation.components.designsystem.FullScreenCircularProgressIndicator

/**
 * Extension function for LazyGridScope to handle different LoadState scenarios and display corresponding UI elements.
 *
 * @param loadState The current LoadState to determine the UI representation.
 */
fun LazyGridScope.onLoadStateRefresh(loadState: LoadState) {
    when (loadState) {
        is LoadState.Error -> {
            items(1, span = { GridItemSpan(maxLineSpan) }) {
                ErrorView(
                    errorMessage = loadState.error.message
                        ?: UiText.StringResource(R.string.unknown_error).asString()
                )
            }
        }

        is LoadState.Loading -> {
            items(1, span = { GridItemSpan(maxLineSpan) }) {
                FullScreenCircularProgressIndicator()
            }
        }

        else -> {}
    }
}

/**
 * Extension function for LazyGridScope to handle different LoadState scenarios for appending items
 * and display corresponding UI elements.
 *
 * @param loadState The current LoadState to determine the UI representation.
 * @param isResultEmpty An indicator indicating whether the list is empty.
 */
fun LazyGridScope.onLoadStateAppend(loadState: LoadState, isResultEmpty: Boolean) {
    when (loadState) {
        is LoadState.Error -> {
            items(1, span = { GridItemSpan(maxLineSpan) }) {
                ErrorView(
                    errorMessage = loadState.error.message
                        ?: UiText.StringResource(R.string.unknown_error).asString()
                )
            }
        }

        is LoadState.Loading -> {
            items(1, span = { GridItemSpan(maxLineSpan) }) {
                FullScreenCircularProgressIndicator()
            }
        }

        is LoadState.NotLoading -> {
            if (isResultEmpty && loadState.endOfPaginationReached) {
                items(1, span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier.size(ComponentDimens.emptyWarningImageSize),
                            painter = painterResource(id = R.drawable.empty),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = Dimens.twoLevelPadding)
                                .padding(horizontal = Dimens.fourLevelPadding),
                            text = stringResource(id = R.string.search_result_empty_text),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
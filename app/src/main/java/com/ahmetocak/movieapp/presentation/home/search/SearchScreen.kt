package com.ahmetocak.movieapp.presentation.home.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.presentation.home.HomeSections
import com.ahmetocak.movieapp.presentation.home.MovieNavigationBar
import com.ahmetocak.movieapp.presentation.ui.components.MovieScaffold
import com.ahmetocak.movieapp.utils.Dimens

@Composable
fun SearchScreen(modifier: Modifier = Modifier, onNavigateToRoute: (String) -> Unit) {

    MovieScaffold(
        modifier = modifier,
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
            searchValue = "",
            onSearchValueChange = {},
            searchError = false,
            searchLabelText = stringResource(id = R.string.search_label_text)
        )
    }
}

@Composable
private fun SearchScreenContent(
    modifier: Modifier,
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    searchError: Boolean,
    searchLabelText: String
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
            }
        )
    }
}

@Composable
private fun SearchResultEmptyView(
    imageId: Int,
    messageId: Int
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(112.dp),
            painter = painterResource(id = imageId),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.twoLevelPadding)
                .padding(horizontal = Dimens.fourLevelPadding),
            text = stringResource(id = messageId),
            textAlign = TextAlign.Center
        )
    }
}
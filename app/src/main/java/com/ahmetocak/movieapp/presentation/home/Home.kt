package com.ahmetocak.movieapp.presentation.home

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.presentation.home.movies.MoviesScreen
import com.ahmetocak.movieapp.presentation.home.profile.ProfileScreen
import com.ahmetocak.movieapp.presentation.home.search.SearchScreen
import com.ahmetocak.movieapp.presentation.home.watch_list.WatchListScreen
import com.ahmetocak.movieapp.utils.SeeAllType

fun NavGraphBuilder.addHomeGraph(
    onNavigateToRoute: (String) -> Unit,
    onMovieClick: (Int, NavBackStackEntry) -> Unit,
    onSeeAllClick: (SeeAllType, NavBackStackEntry) -> Unit,
    onLogOutClick: (NavBackStackEntry) -> Unit
) {
    composable(HomeSections.MOVIES.route) { from ->
        MoviesScreen(
            onMovieClick = remember { { movieId -> onMovieClick(movieId, from) } },
            onSeeAllClick = remember { { seeAllType -> onSeeAllClick(seeAllType, from) } },
            onNavigateToRoute = onNavigateToRoute
        )
    }
    composable(HomeSections.SEARCH.route) {
        SearchScreen(onNavigateToRoute = onNavigateToRoute)
    }
    composable(HomeSections.WATCH_LIST.route) { from ->
        WatchListScreen(
            onMovieClick = remember { { movieId -> onMovieClick(movieId, from) } },
            onNavigateToRoute = onNavigateToRoute
        )
    }
    composable(HomeSections.PROFILE.route) { from ->
        ProfileScreen(
            onNavigateToRoute = onNavigateToRoute,
            onLogOutClick = remember { { onLogOutClick(from) } }
        )
    }
}

enum class HomeSections(
    @StringRes val title: Int,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val route: String
) {
    MOVIES(R.string.movies_text, Icons.Filled.Movie, Icons.Outlined.Movie, "home/movies"),
    SEARCH(R.string.search_text, Icons.Filled.Search, Icons.Outlined.Search, "home/search"),
    WATCH_LIST(
        R.string.watch_list_text,
        Icons.Filled.Bookmark,
        Icons.Outlined.BookmarkBorder,
        "home/watch_list"
    ),
    PROFILE(
        R.string.profile_text,
        Icons.Filled.AccountCircle,
        Icons.Outlined.AccountCircle,
        "home/profile"
    )
}

@Composable
fun MovieNavigationBar(
    tabs: Array<HomeSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit
) {
    val currentSection = tabs.first { it.route == currentRoute }

    NavigationBar {
        tabs.forEach { section ->
            NavigationBarItem(
                selected = currentSection.route == section.route,
                onClick = {
                    if (currentSection.route != section.route) {
                        navigateToRoute(section.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (currentSection.route == section.route)
                            section.selectedIcon else section.unSelectedIcon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(id = section.title))
                }
            )
        }
    }
}
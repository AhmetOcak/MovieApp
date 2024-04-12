package com.ahmetocak.movieapp.presentation.home

import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetocak.common.constants.SeeAllType
import com.ahmetocak.designsystem.WindowSizeClasses
import com.ahmetocak.movies.MoviesScreen
import com.ahmetocak.navigation.HomeSections
import com.ahmetocak.profile.ProfileScreen
import com.ahmetocak.search.SearchScreen
import com.ahmetocak.watch_list.WatchListScreen

fun NavGraphBuilder.addHomeGraph(
    onNavigateToRoute: (String) -> Unit,
    onMovieClick: (Int, NavBackStackEntry) -> Unit,
    onSeeAllClick: (SeeAllType, NavBackStackEntry) -> Unit,
    onLogOutClick: (NavBackStackEntry) -> Unit,
    showNavigationRail: Boolean,
    windowWidthSizeClass: WindowSizeClasses,
    windowHeightSizeClass: WindowSizeClasses
) {
    composable(HomeSections.MOVIES.route) { from ->
        MoviesScreen(
            onMovieClick = remember { { movieId -> onMovieClick(movieId, from) } },
            onSeeAllClick = remember { { seeAllType -> onSeeAllClick(seeAllType, from) } },
            onNavigateToRoute = onNavigateToRoute,
            showNavigationRail = showNavigationRail,
            windowHeightSizeClass = windowHeightSizeClass
        )
    }
    composable(HomeSections.SEARCH.route) { from ->
        SearchScreen(
            onNavigateToRoute = onNavigateToRoute,
            onMovieClick = remember { { movieId -> onMovieClick(movieId, from) } },
            showNavigationRail = showNavigationRail,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }
    composable(HomeSections.WATCH_LIST.route) { from ->
        WatchListScreen(
            onMovieClick = remember { { movieId -> onMovieClick(movieId, from) } },
            onNavigateToRoute = onNavigateToRoute,
            showNavigationRail = showNavigationRail,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }
    composable(HomeSections.PROFILE.route) { from ->
        ProfileScreen(
            onNavigateToRoute = onNavigateToRoute,
            onLogOutClick = remember { { onLogOutClick(from) } },
            showNavigationRail = showNavigationRail
        )
    }
}
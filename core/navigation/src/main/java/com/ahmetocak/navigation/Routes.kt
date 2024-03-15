package com.ahmetocak.navigation

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
import androidx.compose.ui.graphics.vector.ImageVector

object MainDestinations {
    const val LOGIN_ROUTE = "login"
    const val SIGN_UP_ROUTE = "signUp"
    const val MOVIE_DETAILS_ROUTE = "movieDetails"
    const val MOVIE_DETAILS_ID_KEY = "movieId"
    const val SEE_ALL_ROUTE = "seeAll"
    const val SEE_ALL_TYPE_KEY = "seeAllType"
    const val HOME_ROUTE = "home"
    const val ACTOR_DETAILS_ROUTE = "actorDetails"
    const val ACTOR_DETAILS_ID_KEY = "actorId"
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
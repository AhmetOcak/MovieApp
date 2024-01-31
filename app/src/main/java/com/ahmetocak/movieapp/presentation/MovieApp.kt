package com.ahmetocak.movieapp.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.ahmetocak.movieapp.presentation.home.HomeSections
import com.ahmetocak.movieapp.presentation.home.addHomeGraph
import com.ahmetocak.movieapp.presentation.login.LoginScreen
import com.ahmetocak.movieapp.presentation.movie_details.MovieDetailsScreen
import com.ahmetocak.movieapp.presentation.navigation.MainDestinations
import com.ahmetocak.movieapp.presentation.navigation.rememberMovieAppNavController
import com.ahmetocak.movieapp.presentation.see_all.SeeAllScreen
import com.ahmetocak.movieapp.presentation.signup.SignUpScreen
import com.ahmetocak.movieapp.presentation.ui.theme.MovieAppTheme
import com.ahmetocak.movieapp.utils.SeeAllType

@Composable
fun MovieApp(
    startDestination: String,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false
) {
    MovieAppTheme(darkTheme = darkTheme, dynamicColor = dynamicColor) {
        Surface {
            val movieAppNavController = rememberMovieAppNavController()
            NavHost(
                navController = movieAppNavController.navController,
                startDestination = startDestination
            ) {
                movieAppNavGraph(
                    onNavigateToRoute = movieAppNavController::navigateToNavigationBar,
                    upPress = movieAppNavController::upPress,
                    onMovieClick = movieAppNavController::navigateMovieDetails,
                    onSeeAllClick = movieAppNavController::navigateSeeAll,
                    onCreateAccountClick = movieAppNavController::navigateSignUp,
                    onLoginClick = movieAppNavController::navigateToHome,
                    onLogOutClick = movieAppNavController::navigateLogin
                )
            }
        }
    }
}

private fun NavGraphBuilder.movieAppNavGraph(
    onNavigateToRoute: (String) -> Unit,
    upPress: () -> Unit,
    onMovieClick: (Int, NavBackStackEntry) -> Unit,
    onSeeAllClick: (SeeAllType, NavBackStackEntry) -> Unit,
    onCreateAccountClick: (NavBackStackEntry) -> Unit,
    onLoginClick: (NavBackStackEntry) -> Unit,
    onLogOutClick: (NavBackStackEntry) -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.MOVIES.route
    ) {
        addHomeGraph(
            onNavigateToRoute = onNavigateToRoute,
            onMovieClick = onMovieClick,
            onSeeAllClick = onSeeAllClick,
            onLogOutClick = onLogOutClick
        )
    }
    composable(route = MainDestinations.LOGIN_ROUTE) { from ->
        LoginScreen(
            onCreateAccountClick = remember { { onCreateAccountClick(from) } },
            onLoginClick = remember { { onLoginClick(from) } }
        )
    }
    composable(route = MainDestinations.SIGN_UP_ROUTE) {
        SignUpScreen(upPress = upPress)
    }
    composable(
        route = "${MainDestinations.MOVIE_DETAILS_ROUTE}/{${MainDestinations.MOVIE_DETAILS_ID_KEY}}",
        arguments = listOf(
            navArgument(MainDestinations.MOVIE_DETAILS_ID_KEY) { NavType.IntType }
        )
    ) {
        MovieDetailsScreen(upPress = upPress)
    }
    composable(
        route = "${MainDestinations.SEE_ALL_ROUTE}/{${MainDestinations.SEE_ALL_TYPE_KEY}}",
        arguments = listOf(
            navArgument(MainDestinations.SEE_ALL_TYPE_KEY) { NavType.EnumType(SeeAllType::class.java) }
        )
    ) { from ->
        SeeAllScreen(
            upPress = upPress,
            onMovieClick = remember { { movieId -> onMovieClick(movieId, from) } }
        )
    }
}
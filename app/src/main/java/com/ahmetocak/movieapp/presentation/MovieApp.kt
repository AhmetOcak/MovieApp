package com.ahmetocak.movieapp.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.ahmetocak.common.constants.SeeAllType
import com.ahmetocak.login.LoginScreen
import com.ahmetocak.movie_details.MovieDetailsScreen
import com.ahmetocak.movieapp.presentation.home.addHomeGraph
import com.ahmetocak.movieapp.presentation.navigation.rememberMovieAppNavController
import com.ahmetocak.movieapp.presentation.theme.MovieAppTheme
import com.ahmetocak.navigation.HomeSections
import com.ahmetocak.navigation.MainDestinations
import com.ahmetocak.see_all.SeeAllScreen
import com.ahmetocak.signup.SignUpScreen

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
                modifier = Modifier.fillMaxSize(),
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
                    onLogOutClick = movieAppNavController::navigateLogin,
                    onSignUpClick = movieAppNavController::navigateToHome
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
    onLogOutClick: (NavBackStackEntry) -> Unit,
    onSignUpClick: (NavBackStackEntry) -> Unit
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
    composable(route = MainDestinations.SIGN_UP_ROUTE) { from ->
        SignUpScreen(onSignUpClick = remember { { onSignUpClick(from) } })
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
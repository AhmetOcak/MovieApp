package com.ahmetocak.movieapp.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.ahmetocak.actor_details.ActorDetailsScreen
import com.ahmetocak.common.constants.SeeAllType
import com.ahmetocak.designsystem.WindowSizeClasses
import com.ahmetocak.designsystem.components.navigation.MovieNavigationRail
import com.ahmetocak.login.LoginScreen
import com.ahmetocak.movie_details.MovieDetailsScreen
import com.ahmetocak.movieapp.presentation.home.addHomeGraph
import com.ahmetocak.movieapp.presentation.navigation.rememberMovieAppNavController
import com.ahmetocak.designsystem.theme.MovieAppTheme
import com.ahmetocak.navigation.HomeSections
import com.ahmetocak.navigation.MainDestinations
import com.ahmetocak.see_all.SeeAllScreen
import com.ahmetocak.signup.SignUpScreen

@Composable
fun MovieApp(
    startDestination: String,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    windowWidthSizeClass: WindowSizeClasses,
    windowHeightSizeClass: WindowSizeClasses
) {
    MovieAppTheme(darkTheme = darkTheme, dynamicColor = dynamicColor) {
        val movieAppNavController = rememberMovieAppNavController()

        val navBackStackEntry by movieAppNavController.navController.currentBackStackEntryAsState()
        val showNavigationRail =
            windowWidthSizeClass != WindowSizeClasses.COMPACT && isScreenHasNavRail(navBackStackEntry?.destination?.route)

        Surface {
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
                    onSignUpClick = movieAppNavController::navigateToHome,
                    onActorClick = movieAppNavController::navigateToActorDetails,
                    showNavigationRail = showNavigationRail,
                    windowWidthSizeClass = windowWidthSizeClass,
                    windowHeightSizeClass = windowHeightSizeClass
                )
            }
        }
        if (showNavigationRail) {
            MovieNavigationRail(
                tabs = HomeSections.entries.toTypedArray(),
                navigateToRoute = movieAppNavController::navigateToNavigationBar
            )
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
    onSignUpClick: (NavBackStackEntry) -> Unit,
    onActorClick: (Int, NavBackStackEntry) -> Unit,
    showNavigationRail: Boolean,
    windowWidthSizeClass: WindowSizeClasses,
    windowHeightSizeClass: WindowSizeClasses
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.MOVIES.route
    ) {
        addHomeGraph(
            onNavigateToRoute = onNavigateToRoute,
            onMovieClick = onMovieClick,
            onSeeAllClick = onSeeAllClick,
            onLogOutClick = onLogOutClick,
            showNavigationRail = showNavigationRail,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }
    composable(route = MainDestinations.LOGIN_ROUTE) { from ->
        LoginScreen(
            onCreateAccountClick = remember { { onCreateAccountClick(from) } },
            onNavigateToHome = remember { { onLoginClick(from) } },
            windowHeightSizeClass = windowHeightSizeClass
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
    ) { from ->
        MovieDetailsScreen(
            upPress = upPress,
            onActorClick = remember { { actorId -> onActorClick(actorId, from) } },
            onMovieClick = remember { { movieId -> onMovieClick(movieId, from) } },
            windowWidthSizeClass = windowWidthSizeClass
        )
    }
    composable(
        route = "${MainDestinations.SEE_ALL_ROUTE}/{${MainDestinations.SEE_ALL_TYPE_KEY}}",
        arguments = listOf(
            navArgument(MainDestinations.SEE_ALL_TYPE_KEY) { NavType.EnumType(SeeAllType::class.java) }
        )
    ) { from ->
        SeeAllScreen(
            upPress = upPress,
            onMovieClick = remember { { movieId -> onMovieClick(movieId, from) } },
            windowSizeClasses = windowWidthSizeClass
        )
    }
    composable(
        route = "${MainDestinations.ACTOR_DETAILS_ROUTE}/{${MainDestinations.ACTOR_DETAILS_ID_KEY}}",
        arguments = listOf(
            navArgument(MainDestinations.ACTOR_DETAILS_ID_KEY) { NavType.IntType }
        )
    ) { from ->
        ActorDetailsScreen(
            onMovieClick = remember { { movieId -> onMovieClick(movieId, from) } },
            upPress = upPress,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }
}

private fun isScreenHasNavRail(currentScreenRoute: String?): Boolean {
    return HomeSections.entries.find {
        it.route == currentScreenRoute
    } != null
}
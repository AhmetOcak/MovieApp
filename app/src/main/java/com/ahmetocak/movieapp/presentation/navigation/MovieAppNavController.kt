package com.ahmetocak.movieapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ahmetocak.movieapp.presentation.home.HomeSections
import com.ahmetocak.movieapp.utils.SeeAllType

object MainDestinations {
    const val LOGIN_ROUTE = "login"
    const val SIGN_UP_ROUTE = "signUp"
    const val MOVIE_DETAILS_ROUTE = "movieDetails"
    const val MOVIE_DETAILS_ID_KEY = "movieId"
    const val SEE_ALL_ROUTE = "seeAll"
    const val SEE_ALL_TYPE_KEY = "seeAllType"
    const val HOME_ROUTE = "home"
}

@Composable
fun rememberMovieAppNavController(
    navController: NavHostController = rememberNavController()
): MovieAppNavController = remember(navController) {
    MovieAppNavController(navController)
}

@Stable
class MovieAppNavController(val navController: NavHostController) {

    private val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToNavigationBar(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                popUpTo(HomeSections.MOVIES.route) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    fun navigateMovieDetails(movieId: Int, from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate("${MainDestinations.MOVIE_DETAILS_ROUTE}/$movieId")
        }
    }

    fun navigateSeeAll(seeAllType: SeeAllType, from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate("${MainDestinations.SEE_ALL_ROUTE}/$seeAllType")
        }
    }

    fun navigateLogin(from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate(MainDestinations.LOGIN_ROUTE) {
                popUpTo(0)
            }
        }
    }

    fun navigateSignUp(from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate(MainDestinations.SIGN_UP_ROUTE)
        }
    }

    fun navigateToHome(from: NavBackStackEntry) {
        if (shouldNavigate(from)) {
            navController.navigate(MainDestinations.HOME_ROUTE) {
                popUpTo(0)
            }
        }
    }
}

private fun shouldNavigate(from: NavBackStackEntry): Boolean = from.isLifecycleResumed()

private fun NavBackStackEntry.isLifecycleResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
package com.ahmetocak.movieapp.presentation

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ahmetocak.common.helpers.LocaleManager
import com.ahmetocak.designsystem.WindowSizeClasses
import com.ahmetocak.designsystem.computeWindowHeightSize
import com.ahmetocak.designsystem.computeWindowWidthSize
import com.ahmetocak.movieapp.presentation.navigation.rememberMovieAppNavController
import com.ahmetocak.navigation.HomeSections
import com.ahmetocak.navigation.MainDestinations
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val movieAppNavController = rememberMovieAppNavController()
            val navBackStackEntry by movieAppNavController.navController.currentBackStackEntryAsState()

            MovieApp(
                startDestination = if (firebaseAuth.currentUser?.uid == null)
                    MainDestinations.LOGIN_ROUTE else MainDestinations.HOME_ROUTE,
                darkTheme = uiState.isDarkModeOn,
                dynamicColor = uiState.isDynamicColorOn,
                windowWidthSizeClass = computeWindowWidthSize() ?: WindowSizeClasses.COMPACT,
                windowHeightSizeClass = computeWindowHeightSize() ?: WindowSizeClasses.MEDIUM,
                movieAppNavController = movieAppNavController,
                currentRoute = navBackStackEntry?.destination?.route ?: HomeSections.MOVIES.route
            )

            if (uiState.userMessages.isNotEmpty()) {
                Toast.makeText(
                    this.applicationContext,
                    uiState.userMessages.first().asString(),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.consumedUserMessage()
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        newBase?.let { context ->
            val localeManager = LocaleManager(context)
            val languageCode: String = localeManager.getAppLanguage()
            super.attachBaseContext(localeManager.getLocale(languageCode))
        }
    }
}
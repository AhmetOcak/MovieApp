package com.ahmetocak.movieapp.presentation

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ahmetocak.movieapp.presentation.navigation.MainDestinations
import com.ahmetocak.movieapp.utils.LocaleManager
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
        setContent {
            val uiState by viewModel.uiState.collectAsState()

            MovieApp(
                startDestination = if (firebaseAuth.currentUser?.uid == null)
                    MainDestinations.LOGIN_ROUTE else MainDestinations.HOME_ROUTE,
                darkTheme = uiState.isDarkModeOn,
                dynamicColor = uiState.isDynamicColorOn
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
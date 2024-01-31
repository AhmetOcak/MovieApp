package com.ahmetocak.movieapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ahmetocak.movieapp.presentation.navigation.MainDestinations
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.collectAsState()

            MovieApp(
                startDestination = MainDestinations.LOGIN_ROUTE,
                darkTheme = uiState.isDarkModeOn,
                dynamicColor = uiState.isDynamicColorOn
            )
        }
    }
}
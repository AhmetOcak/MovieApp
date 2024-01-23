package com.ahmetocak.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ahmetocak.movieapp.presentation.MovieApp
import com.ahmetocak.movieapp.presentation.navigation.MainDestinations

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieApp(startDestination = MainDestinations.HOME_ROUTE)
        }
    }
}
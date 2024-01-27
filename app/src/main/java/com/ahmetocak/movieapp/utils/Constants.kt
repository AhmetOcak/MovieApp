package com.ahmetocak.movieapp.utils

object TMDB {
    const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"
}

object DataStoreConstants {
    const val FILE_NAME = "movie_app_preferences"
    const val APP_THEME_KEY = "app_theme_preference"
}

object Network {
    const val BASE_URL = "https://api.themoviedb.org/"
    object EndPoints {
        const val NOW_PLAYING = "/3/movie/now_playing"
        const val POPULAR = "/3/movie/popular"
        const val MOVIE_DETAILS = "/3/movie/{movie_id}"
    }

    object Queries {
        const val PAGE = "page"
        const val API_KEY = "api_key"
    }
}
package com.ahmetocak.movieapp.utils

object TMDB {
    const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"
    const val JOB_DIRECTOR_KEY = "Director"
}

object DataStoreConstants {
    const val FILE_NAME = "movie_app_preferences"
    const val APP_THEME_KEY = "app_theme_preference"
    const val DYNAMIC_COLOR = "dynamic_color_preference"
    const val APP_LANGUAGE_FILE_NAME = "app_language_file"
}

object Network {
    const val BASE_URL = "https://api.themoviedb.org/"
    object EndPoints {
        const val NOW_PLAYING = "/3/movie/now_playing"
        const val POPULAR = "/3/movie/popular"
        const val MOVIE_DETAILS = "/3/movie/{${Paths.MOVIE_ID}}"
        const val MOVIE_CREDITS = "/3/movie/{${Paths.MOVIE_ID}}/credits"
        const val MOVIE_TRAILERS = "/3/movie/{${Paths.MOVIE_ID}}/videos"
        const val SEARCH_MOVIE = "/3/search/movie"
    }

    object Queries {
        const val PAGE = "page"
        const val API_KEY = "api_key"
        const val SEARCH_QUERY = "query"
    }

    object Paths {
        const val MOVIE_ID = "movie_id"
    }
}

object Firestore {
    const val WATCH_LIST_COLLECTION_KEY = "movie_watch_lists"
    const val WATCH_LIST_ARRAY_NAME = "watchList"
}

object Database {
    const val WATCH_LIST_DB_NAME =  "watch_list_db"
}

object Firestorage {
    const val USER_PROFILE_IMG_PATH = "user_profile_images"
}
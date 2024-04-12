package com.ahmetocak.network.helpers

object NetworkConstants {
    const val BASE_URL = "https://api.themoviedb.org/"
    object EndPoints {
        const val TRENDING = "/3/trending/movie/{${Paths.TIME}}"
        const val TOP_RATED = "/3/movie/top_rated"
        const val UPCOMING = "/3/movie/upcoming"
        const val MOVIE_DETAILS = "/3/movie/{${Paths.MOVIE_ID}}"
        const val MOVIE_CREDITS = "/3/movie/{${Paths.MOVIE_ID}}/credits"
        const val MOVIE_TRAILERS = "/3/movie/{${Paths.MOVIE_ID}}/videos"
        const val SEARCH_MOVIE = "/3/search/movie"
        const val ACTOR_DETAILS = "/3/person/{${Paths.ACTOR_ID}}"
        const val ACTOR_MOVIES = "/3/person/{${Paths.ACTOR_ID}}/movie_credits"
        const val USER_REVIEWS = "/3/movie/{${Paths.MOVIE_ID}}/reviews"
        const val RECOMMENDATIONS = "/3/movie/{${Paths.MOVIE_ID}}/recommendations"
    }

    object Queries {
        const val PAGE = "page"
        const val API_KEY = "api_key"
        const val SEARCH_QUERY = "query"
        const val LANGUAGE = "language"
    }

    object Paths {
        const val MOVIE_ID = "movie_id"
        const val ACTOR_ID = "actor_id"
        const val TIME = "time_window"
    }
}

object FirebaseConstants {
    object Firestore {
        const val WATCH_LIST_COLLECTION_NAME = "movie_watch_lists"
        const val WATCH_LIST_ARRAY_NAME = "watchList"
    }

    object Firestorage {
        const val USER_PROFILE_IMG_DIRECTORY_NAME = "user_profile_images"
    }
}
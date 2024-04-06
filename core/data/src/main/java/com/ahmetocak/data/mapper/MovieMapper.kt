package com.ahmetocak.data.mapper

import com.ahmetocak.database.entity.WatchListEntity
import com.ahmetocak.model.firebase.WatchListMovie
import com.ahmetocak.model.movie.ActorDetails
import com.ahmetocak.model.movie.ActorMovies
import com.ahmetocak.model.movie.ActorMoviesContent
import com.ahmetocak.model.movie.Movie
import com.ahmetocak.model.movie.MovieContent
import com.ahmetocak.model.movie.RecommendedMovieContent
import com.ahmetocak.model.movie.UserReviewAuthorDetails
import com.ahmetocak.model.movie.UserReviewResults
import com.ahmetocak.model.movie_detail.Cast
import com.ahmetocak.model.movie_detail.MovieCredit
import com.ahmetocak.model.movie_detail.MovieDetail
import com.ahmetocak.model.movie_detail.MovieTrailer
import com.ahmetocak.model.movie_detail.Trailer
import com.ahmetocak.model.watch_list.WatchList
import com.ahmetocak.network.model.movie.NetworkActorDetails
import com.ahmetocak.network.model.movie.NetworkActorMovies
import com.ahmetocak.network.model.movie.NetworkActorMoviesContent
import com.ahmetocak.network.model.movie.NetworkMovie
import com.ahmetocak.network.model.movie.NetworkMovieContent
import com.ahmetocak.network.model.movie.NetworkRecommendedMovieContent
import com.ahmetocak.network.model.movie.NetworkUserReviewResults
import com.ahmetocak.network.model.movie_detail.NetworkMovieCredit
import com.ahmetocak.network.model.movie_detail.NetworkMovieDetail
import com.ahmetocak.network.model.movie_detail.NetworkMovieTrailer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun NetworkMovie.toMovie(): Movie {
    return Movie(
        movies = movies.map { it.toMovieContent() },
        totalPages = totalPages
    )
}

internal fun NetworkMovieContent.toMovieContent(): MovieContent {
    return MovieContent(
        id = id,
        backdropImagePath = backdropImagePath,
        posterImagePath = posterImagePath,
        releaseDate = releaseDate?: "",
        movieName = movieName ?: "",
        voteAverage = voteAverage ?: 0.0,
        voteCount = voteCount ?: 0
    )
}

internal fun NetworkMovieDetail.toMovieDetail(): MovieDetail {
    return MovieDetail(
        id = id,
        genres = genres.joinToString { it.name },
        overview = overview ?: "",
        posterImageUrlPath = posterImageUrlPath ?: "",
        backdropImageUrlPath = backdropImageUrlPath ?: "",
        movieName = movieName ?: "",
        voteAverage = voteAverage?.toFloat() ?: 0f,
        voteCount = voteCount ?: 0,
        releaseDate = releaseDate ?: "",
        duration = duration ?: 0,
        originalMovieName = originalMovieName ?: ""
    )
}

internal fun NetworkMovieCredit.toMovieCredit(): MovieCredit {
    return MovieCredit(
        cast = cast.map { castDto ->
            Cast(
                id = castDto.id,
                name = castDto.name ?: "",
                characterName = castDto.characterName ?: "",
                imageUrlPath = castDto.imageUrlPath ?: ""
            )
        },
        directorName = crew.firstOrNull { crewDto ->
            crewDto.job == "Director"
        }?.name ?: ""
    )
}

internal fun WatchListMovie.toWatchListEntity(): WatchListEntity {
    return WatchListEntity(
        movieId = id ?: -1,
        movieName = name ?: "",
        releaseYear = releaseYear ?: "",
        voteAverage = voteAverage ?: 0f,
        voteCount = voteCount ?: 0,
        imageUrlPath = imageUrlPath
    )
}

internal fun Flow<List<WatchListEntity>>.toWatchList(): Flow<List<WatchList>> {
    return this.map {
        it.map { entity ->
            WatchList(
                movieId = entity.movieId,
                movieName = entity.movieName,
                releaseYear = entity.releaseYear,
                voteAverage = entity.voteAverage,
                voteCount = entity.voteCount,
                imageUrlPath = entity.imageUrlPath
            )
        }
    }
}

internal fun NetworkMovieTrailer.toMovieTrailer(): MovieTrailer {
    return MovieTrailer(
        trailers = trailers.map {
            Trailer(
                name = it.name,
                key = it.key
            )
        }
    )
}

internal fun NetworkActorDetails.toActorDetails(): ActorDetails {
    return ActorDetails(
        id = id,
        biography = biography ?: "",
        birthday = birthday ?: "",
        homepage = homepage,
        name = name ?: "",
        deathDay = deathDay,
        placeOfBirth = placeOfBirth ?: "",
        profileImagePath = profileImagePath ?: ""
    )
}

internal fun NetworkActorMovies.toActorMovies(): ActorMovies {
    return ActorMovies(movies = this.movies.map { it.toActorMoviesContent() })
}

private fun NetworkActorMoviesContent.toActorMoviesContent(): ActorMoviesContent {
    return ActorMoviesContent(
        id = id,
        movieName = movieName ?: "",
        posterImagePath = posterImagePath,
        releaseDate = releaseDate ?: "",
        voteAverage = voteAverage ?: 0.0,
        voteCount = voteCount ?: 0
    )
}

internal fun NetworkUserReviewResults.toUserReviewResults(): UserReviewResults {
    return UserReviewResults(
        id = id,
        author = author ?: "anonymous",
        content = content ?: "",
        createdAt = createdAt,
        updatedAt = updatedAt ?: "",
        authorDetails = UserReviewAuthorDetails(
            rating = authorDetails.rating,
            avatarPath = authorDetails.avatarPath
        )
    )
}

internal fun NetworkRecommendedMovieContent.toRecommendedMovieContent(): RecommendedMovieContent {
    return RecommendedMovieContent(
        id = id,
        movieName = movieName ?: "",
        image = image ?: "",
        releaseDate = releaseDate ?: "",
        voteAverage = voteAverage ?: 0.0,
        voteCount = voteCount ?: 0
    )
}
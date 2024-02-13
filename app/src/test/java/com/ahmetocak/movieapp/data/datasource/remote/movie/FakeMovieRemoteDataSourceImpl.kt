package com.ahmetocak.movieapp.data.datasource.remote.movie

import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.model.movie.Movie
import com.ahmetocak.movieapp.model.movie.MovieContent
import com.ahmetocak.movieapp.model.movie_detail.CastDto
import com.ahmetocak.movieapp.model.movie_detail.CrewDto
import com.ahmetocak.movieapp.model.movie_detail.MovieCreditDto
import com.ahmetocak.movieapp.model.movie_detail.MovieDetailDto
import com.ahmetocak.movieapp.model.movie_detail.MovieGenreDto
import com.ahmetocak.movieapp.model.movie_detail.MovieTrailer
import com.ahmetocak.movieapp.model.movie_detail.Trailer
import com.ahmetocak.movieapp.utils.TMDB

class FakeMovieRemoteDataSourceImpl : MovieRemoteDataSource {

    override suspend fun getNowPlayingMoviesFirstPage(): Response<Movie> {
        return Response.Success(expectedMovieListData)
    }

    override suspend fun getPopularMoviesFirstPage(): Response<Movie> {
        return Response.Success(expectedMovieListData)
    }

    override suspend fun getMovieDetails(movieId: Int): Response<MovieDetailDto> {
        return Response.Success(expectedMovieDetailData)
    }

    override suspend fun getMovieCredits(movieId: Int): Response<MovieCreditDto> {
        return Response.Success(expectedMovieCreditsData)
    }

    override suspend fun getMovieTrailers(movieId: Int): Response<MovieTrailer> {
        return Response.Success(expectedMovieTrailersData)
    }

    companion object {
        val expectedMovieListData = Movie(
            movies = listOf(
                MovieContent(
                    id = 0,
                    backdropImagePath = "fake backdropImagePath",
                    posterImagePath = "fake posterImagePath",
                    releaseDate = "fake releaseDate",
                    movieName = "fake movieName",
                    voteAverage = 0.0,
                    voteCount = 0
                )
            ),
            totalPages = 1
        )

        val expectedMovieDetailData = MovieDetailDto(
            id = 0,
            genres = listOf(MovieGenreDto("fake genre")),
            overview = "fake overview",
            imageUrlPath = "fake imageUrlPath",
            movieName = "fake movieName",
            voteAverage = 0.0,
            voteCount = 0,
            releaseDate = "fake releaseDate",
            duration = 0,
            originalMovieName = "fake originalMovieName"
        )

        val expectedMovieCreditsData = MovieCreditDto(
            cast = listOf(
                CastDto(
                    id = 0,
                    name = "fake name",
                    characterName = "fake characterName",
                    imageUrlPath = "fake imageUrlPath"
                )
            ),
            crew = listOf(
                CrewDto(name = "fake crew 1", job = "fake job"),
                CrewDto(name = "fake crew 2", job = TMDB.JOB_DIRECTOR_KEY)
            )
        )

        val expectedMovieTrailersData = MovieTrailer(
            trailers = listOf(
                Trailer(name = "fake name", key = "fake key")
            )
        )
    }
}
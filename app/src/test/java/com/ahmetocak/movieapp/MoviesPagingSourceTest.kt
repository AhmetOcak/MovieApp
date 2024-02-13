package com.ahmetocak.movieapp

import androidx.paging.PagingSource
import com.ahmetocak.movieapp.data.datasource.remote.movie.api.MovieApi
import com.ahmetocak.movieapp.data.datasource.remote.movie.paging_source.MoviesPagingSource
import com.ahmetocak.movieapp.model.movie.Movie
import com.ahmetocak.movieapp.model.movie.MovieContent
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class MoviesPagingSourceTest {

    private val movieData = Movie(
        movies = listOf(
            MovieContent(
                id = 0,
                backdropImagePath = "fake",
                posterImagePath = "fake",
                releaseDate = "fake",
                movieName = "fake",
                voteAverage = 0.0,
                voteCount = 0
            ),
            MovieContent(
                id = 1,
                backdropImagePath = "fake 1",
                posterImagePath = "fake 1",
                releaseDate = "fake 1",
                movieName = "fake 1",
                voteAverage = 1.0,
                voteCount = 1
            )
        ),
        totalPages = 1
    )

    @Mock
    lateinit var api: MovieApi

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun getAllPopularMovies_returnExceptedData() {
        runTest {
            whenever(api.getPopularMovies()).thenReturn(movieData)

            val moviesPagingSource = MoviesPagingSource(
                apiCall = {
                    api.getPopularMovies()
                }
            )

            val params = PagingSource
                .LoadParams
                .Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )

            val expected = PagingSource
                .LoadResult
                .Page(
                    data = movieData.movies,
                    prevKey = null,
                    nextKey = 1
                )

            val actualData = moviesPagingSource.load(params = params)

            assertEquals(expected, actualData)
        }
    }
}
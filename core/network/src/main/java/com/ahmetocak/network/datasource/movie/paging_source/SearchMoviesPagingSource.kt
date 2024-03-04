package com.ahmetocak.network.datasource.movie.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ahmetocak.network.api.MovieApi
import com.ahmetocak.network.model.movie.NetworkMovieContent

class SearchMoviesPagingSource(
    private val query: String,
    private val api: MovieApi
): PagingSource<Int, NetworkMovieContent>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkMovieContent> {
        return try {
            val currentPageNumber = params.key ?: 1
            val response = api.searchMovie(query = query, page = currentPageNumber)
            LoadResult.Page(
                data = response.movies.filter { it.posterImagePath != null },
                prevKey = null,
                nextKey = if (currentPageNumber < response.totalPages) currentPageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NetworkMovieContent>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
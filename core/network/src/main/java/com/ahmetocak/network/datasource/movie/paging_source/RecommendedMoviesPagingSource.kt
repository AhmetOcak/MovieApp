package com.ahmetocak.network.datasource.movie.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ahmetocak.network.api.MovieApi
import com.ahmetocak.network.model.movie.NetworkRecommendedMovieContent

class RecommendedMoviesPagingSource(
    private val movieId: Int,
    private val api: MovieApi
): PagingSource<Int, NetworkRecommendedMovieContent>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkRecommendedMovieContent> {
        return try {
            val currentPageNumber = params.key ?: 1
            val response = api.getMovieRecommendations(movieId = movieId, page = currentPageNumber)
            LoadResult.Page(
                data = response.recommendations.filter { it.image != null },
                prevKey = null,
                nextKey = if (currentPageNumber < response.totalPages) currentPageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NetworkRecommendedMovieContent>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
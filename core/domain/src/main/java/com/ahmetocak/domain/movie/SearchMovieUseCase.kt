package com.ahmetocak.domain.movie

import com.ahmetocak.data.repository.movie.MovieRepository
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor(private val repository: MovieRepository) {

    operator fun invoke(query: String) = repository.searchMovie(query = query)
}
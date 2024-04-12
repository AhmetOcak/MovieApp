package com.ahmetocak.domain.movie

import com.ahmetocak.data.repository.movie.MovieRepository
import javax.inject.Inject

class GetAllUpcomingMoviesUseCase @Inject constructor(private val repository: MovieRepository) {

    operator fun invoke() = repository.getAllUpcomingMovies()
}
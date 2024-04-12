package com.ahmetocak.domain.movie

import com.ahmetocak.data.repository.movie.MovieRepository
import javax.inject.Inject

class GetUpcomingMoviesFirstPageUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend operator fun invoke() = movieRepository.getUpcomingMoviesFirstPage()
}
package com.ahmetocak.domain.movie

import com.ahmetocak.data.repository.movie.MovieRepository
import javax.inject.Inject

class GetUserMovieReviewsUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    operator fun invoke(movieId: Int) = movieRepository.getUserMovieReviews(movieId)
}
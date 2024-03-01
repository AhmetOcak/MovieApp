package com.ahmetocak.domain.movie

import com.ahmetocak.data.repository.movie.MovieRepository
import javax.inject.Inject

class GetMovieCreditsUseCase @Inject constructor(private val repository: MovieRepository) {

    suspend operator fun invoke(movieId: Int) = repository.getMovieCredits(movieId = movieId)
}
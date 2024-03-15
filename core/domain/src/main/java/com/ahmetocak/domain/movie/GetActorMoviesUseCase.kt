package com.ahmetocak.domain.movie

import com.ahmetocak.data.repository.movie.MovieRepository
import javax.inject.Inject

class GetActorMoviesUseCase @Inject constructor(private val repository: MovieRepository) {

    suspend operator fun invoke(actorId: Int) = repository.getActorMovies(actorId)
}
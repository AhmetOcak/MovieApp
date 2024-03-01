package com.ahmetocak.domain.movie

import com.ahmetocak.data.repository.movie.MovieRepository
import com.ahmetocak.model.firebase.WatchListMovie
import javax.inject.Inject

class AddMovieToDbWatchListUseCase @Inject constructor(private val repository: MovieRepository) {

    suspend operator fun invoke(watchListMovie: WatchListMovie) =
        repository.addMovieToWatchList(watchListMovie = watchListMovie)
}
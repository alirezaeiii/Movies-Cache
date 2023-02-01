package com.sample.movies.usecase

import com.sample.movies.domain.Movie
import com.sample.movies.repository.MovieRepository
import com.sample.movies.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(): Flow<Resource<List<Movie>>> =
        movieRepository.getMovies()
}
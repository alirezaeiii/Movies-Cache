package com.zattoo.movies.usecase

import com.zattoo.movies.data.Movie
import com.zattoo.movies.repository.MovieRepository
import com.zattoo.movies.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(): Flow<Resource<List<Movie>>> =
        movieRepository.getMovies()
}
package com.sample.movies.repository

import com.sample.movies.domain.Movie
import com.sample.movies.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(): Flow<Resource<List<Movie>>>
}
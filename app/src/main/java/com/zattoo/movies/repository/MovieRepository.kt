package com.zattoo.movies.repository

import com.zattoo.movies.data.Movie
import com.zattoo.movies.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(): Flow<Resource<List<Movie>>>
}
package com.zattoo.movies.repository

import com.zattoo.movies.data.home.Movie
import com.zattoo.movies.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(): Flow<Resource<List<Movie>>>
}
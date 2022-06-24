package com.zattoo.movies.repository

import android.content.Context
import com.zattoo.movies.R
import com.zattoo.movies.data.MovieListEntity
import com.zattoo.movies.data.MovieListOffers
import com.zattoo.movies.data.MovieService
import com.zattoo.movies.data.home.Movie
import com.zattoo.movies.di.IoDispatcher
import com.zattoo.movies.utils.Resource
import com.zattoo.movies.utils.createMovies
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val context: Context,
    private val movieService: MovieService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MovieRepository {

    override fun getMovies(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            coroutineScope {
                val movieDataList: Deferred<MovieListEntity> = async {
                    movieService.fetchMovieList()
                }
                val movieListOffers: Deferred<MovieListOffers> = async {
                    movieService.fetchMovieListOffers()
                }

                emit(
                    Resource.Success(
                        createMovies(
                            movieDataList.await().movie_data,
                            movieListOffers.await()
                        )
                    )
                )
            }
        } catch (t: Throwable) {
            emit(Resource.Error(context.getString(R.string.general_error_message)))
        }
    }.flowOn(ioDispatcher)
}
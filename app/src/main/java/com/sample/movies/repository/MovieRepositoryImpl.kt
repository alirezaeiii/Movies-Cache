package com.sample.movies.repository

import android.content.Context
import com.sample.movies.data.Movie
import com.sample.movies.data.MovieListEntity
import com.sample.movies.data.MovieListOffers
import com.sample.movies.data.MovieService
import com.sample.movies.di.IoDispatcher
import com.sample.movies.utils.Resource
import com.sample.movies.utils.createMovies
import com.sample.movies.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
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
        } catch (ex: HttpException) {
            when (ex.code()) {
                403 -> emit(Resource.Error(context.getString(R.string.forbidden_error_message)))
                404 -> emit(Resource.Error(context.getString(R.string.not_found_error_message)))
                500 -> emit(Resource.Error(context.getString(R.string.internal_server_error_message)))
                else -> emit(Resource.Error(context.getString(R.string.general_error_message)))
            }
        } catch (t: Throwable) {
            emit(Resource.Error(context.getString(R.string.general_error_message)))
        }
    }.flowOn(ioDispatcher)
}
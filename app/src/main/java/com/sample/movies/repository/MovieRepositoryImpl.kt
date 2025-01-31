package com.sample.movies.repository

import android.content.Context
import com.sample.movies.R
import com.sample.movies.data.response.MovieListResponse
import com.sample.movies.data.response.MovieListOffers
import com.sample.movies.data.network.MovieService
import com.sample.movies.data.repository.MovieRepository
import com.sample.movies.database.MovieDao
import com.sample.movies.database.asDomainModel
import com.sample.movies.di.IoDispatcher
import com.sample.movies.domain.Movie
import com.sample.movies.domain.asDatabaseModel
import com.sample.movies.utils.Resource
import com.sample.movies.utils.createMovies
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dao: MovieDao,
    private val movieService: MovieService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MovieRepository {

    override fun getMovies(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            dao.getMovies()?.let {
                // ****** SINGLE SOURCE OF TRUTH - VIEW CACHE ******
                emit(Resource.Success(it.asDomainModel()))
                emitMovies(true)
            } ?: run {
                emitMovies()
            }
        } catch (t: Throwable) {
            emit(Resource.Error(context.getString(R.string.general_error_message)))
        }

    }.flowOn(ioDispatcher)

    private suspend fun FlowCollector<Resource<List<Movie>>>.emitMovies(
        shouldClearDb: Boolean = false
    ) {
        // ****** MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
        coroutineScope {
            val movieDataList: Deferred<MovieListResponse> = async {
                movieService.fetchMovieList()
            }
            val movieListOffers: Deferred<MovieListOffers> = async {
                movieService.fetchMovieListOffers()
            }

            val movies = createMovies(
                movieDataList.await().movieData,
                movieListOffers.await()
            )

            if (shouldClearDb) {
                dao.deleteMovies()
            }
            dao.insert(movies.asDatabaseModel())

            // ****** SINGLE SOURCE OF TRUTH - VIEW CACHE ******
            emit(Resource.Success(dao.getMovies()?.asDomainModel(), hideRefreshing = true))
        }
    }
}
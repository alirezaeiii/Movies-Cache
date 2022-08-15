package com.sample.movies.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.sample.movies.TestCoroutineRule
import com.sample.movies.data.MovieListEntity
import com.sample.movies.data.MovieListOffers
import com.sample.movies.data.MovieService
import com.sample.movies.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var movieService: MovieService

    @Mock
    private lateinit var context: Context

    @Test
    fun `test Api Succeeds`() {
        testCoroutineRule.runBlockingTest {
            whenever(movieService.fetchMovieList()).thenReturn(MovieListEntity(emptyList()))
            whenever(movieService.fetchMovieListOffers()).thenReturn(
                MovieListOffers(
                    "",
                    emptyList()
                )
            )

            val repository = MovieRepositoryImpl(context, movieService, Dispatchers.Main)

            assertThat(repository.getMovies().first(), `is`(Resource.Loading))
            assertThat(repository.getMovies().last(), `is`(Resource.Success(emptyList())))
        }
    }

    @Test
    fun `test Fetch MovieList Fails`() {
        val errorMsg = "error message"
        `when`(context.getString(anyInt())).thenReturn(errorMsg)

        testCoroutineRule.runBlockingTest {
            whenever(movieService.fetchMovieList()).thenThrow(RuntimeException(""))

            val repository = MovieRepositoryImpl(context, movieService, Dispatchers.Main)

            assertThat(repository.getMovies().first(), `is`(Resource.Loading))
            assertThat(repository.getMovies().last(), `is`(Resource.Error(errorMsg)))
        }
    }

    @Test
    fun `test Fetch MovieListOffers Fails`() {
        val errorMsg = "error message"
        `when`(context.getString(anyInt())).thenReturn(errorMsg)

        testCoroutineRule.runBlockingTest {
            whenever(movieService.fetchMovieListOffers()).thenThrow(RuntimeException(""))

            val repository = MovieRepositoryImpl(context, movieService, Dispatchers.Main)

            assertThat(repository.getMovies().first(), `is`(Resource.Loading))
            assertThat(repository.getMovies().last(), `is`(Resource.Error(errorMsg)))
        }
    }
}
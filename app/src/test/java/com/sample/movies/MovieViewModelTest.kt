package com.sample.movies

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.sample.movies.data.MovieListEntity
import com.sample.movies.data.MovieListOffers
import com.sample.movies.data.MovieService
import com.sample.movies.repository.MovieRepositoryImpl
import com.sample.movies.usecase.GetMoviesUseCase
import com.sample.movies.utils.Resource
import com.sample.movies.viewmodel.MovieViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
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
class MovieViewModelTest {

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
        }
        testCoroutineRule.pauseDispatcher()

        val viewModel = getMovieViewModel()

        assertThat(viewModel.stateFlow.value, `is`(Resource.Loading))

        testCoroutineRule.resumeDispatcher()

        assertThat(viewModel.stateFlow.value, `is`(Resource.Success(emptyList())))
    }

    @Test
    fun `test Fetch MovieList Fails`() {
        val errorMsg = "error message"
        `when`(context.getString(anyInt())).thenReturn(errorMsg)

        testCoroutineRule.runBlockingTest {
            whenever(movieService.fetchMovieList()).thenThrow(RuntimeException(""))
        }
        testCoroutineRule.pauseDispatcher()

        val viewModel = getMovieViewModel()

        assertThat(viewModel.stateFlow.value, `is`(Resource.Loading))

        testCoroutineRule.resumeDispatcher()

        assertThat(viewModel.stateFlow.value, `is`(Resource.Error(errorMsg)))
    }

    @Test
    fun `test Fetch MovieListOffers Fails`() {
        val errorMsg = "error message"
        `when`(context.getString(anyInt())).thenReturn(errorMsg)

        testCoroutineRule.runBlockingTest {
            whenever(movieService.fetchMovieListOffers()).thenThrow(RuntimeException(""))
        }
        testCoroutineRule.pauseDispatcher()

        val viewModel = getMovieViewModel()

        assertThat(viewModel.stateFlow.value, `is`(Resource.Loading))

        testCoroutineRule.resumeDispatcher()

        assertThat(viewModel.stateFlow.value, `is`(Resource.Error(errorMsg)))
    }

    private fun getMovieViewModel(): MovieViewModel {
        val repository = MovieRepositoryImpl(context, movieService, Dispatchers.Main)
        val useCase = GetMoviesUseCase(repository)
        return MovieViewModel(useCase)
    }
}
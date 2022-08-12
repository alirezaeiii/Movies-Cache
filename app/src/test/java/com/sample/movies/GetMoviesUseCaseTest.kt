package com.sample.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.sample.movies.data.Movie
import com.sample.movies.repository.MovieRepository
import com.sample.movies.usecase.GetMoviesUseCase
import com.sample.movies.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetMoviesUseCaseTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: MovieRepository

    @Test
    fun `test Api Succeeds`() {
        testCoroutineRule.runBlockingTest {
            whenever(repository.getMovies()).thenReturn(
                flowOf(Resource.Success(emptyList()))
            )

            val useCase = GetMoviesUseCase(repository)

            assertEquals(Resource.Success<List<Movie>>(emptyList()), useCase.invoke().first())
        }
    }

    @Test
    fun `test Fetch MovieList Fails`() {
        val errorMsg = "error message"

        testCoroutineRule.runBlockingTest {
            whenever(repository.getMovies()).thenReturn(
                flowOf(Resource.Error(errorMsg))
            )

            val useCase = GetMoviesUseCase(repository)

            assertEquals(Resource.Error(errorMsg), useCase.invoke().first())
        }
    }
}
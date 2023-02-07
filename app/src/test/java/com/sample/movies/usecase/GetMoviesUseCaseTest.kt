package com.sample.movies.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.sample.movies.TestCoroutineRule
import com.sample.movies.repository.MovieRepository
import com.sample.movies.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
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

    private lateinit var useCase: GetMoviesUseCase

    @Before
    fun setup() {
        useCase = GetMoviesUseCase(repository)
    }

    @Test
    fun `test Api Succeeds`() {
        testCoroutineRule.runBlockingTest {
            whenever(repository.getMovies()).thenReturn(
                flowOf(Resource.Loading, Resource.Success(emptyList()))
            )

            assertThat(useCase.invoke().first(), `is`(Resource.Loading))
            assertThat(useCase.invoke().last(), `is`(Resource.Success(emptyList())))
        }
    }

    @Test
    fun `test Fetch MovieList Fails`() {
        val errorMsg = "error message"
        testCoroutineRule.runBlockingTest {
            whenever(repository.getMovies()).thenReturn(
                flowOf(Resource.Loading, Resource.Error(errorMsg))
            )

            assertThat(useCase.invoke().first(), `is`(Resource.Loading))
            assertThat(useCase.invoke().last(), `is`(Resource.Error(errorMsg)))
        }
    }
}
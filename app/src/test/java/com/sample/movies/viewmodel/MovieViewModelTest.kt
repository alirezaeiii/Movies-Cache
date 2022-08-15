package com.sample.movies.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.sample.movies.TestCoroutineRule
import com.sample.movies.usecase.GetMoviesUseCase
import com.sample.movies.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
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
    private lateinit var useCase: GetMoviesUseCase

    @Test
    fun `test Api Succeeds`() {
        testCoroutineRule.runBlockingTest {
            whenever(useCase.invoke()).thenReturn(
                flowOf(Resource.Success(emptyList()))
            )
        }
        testCoroutineRule.pauseDispatcher()

        val viewModel = MovieViewModel(useCase)

        assertThat(viewModel.stateFlow.value, `is`(Resource.Loading))

        testCoroutineRule.resumeDispatcher()

        assertThat(viewModel.stateFlow.value, `is`(Resource.Success(emptyList())))
    }

    @Test
    fun `test Fetch MovieList Fails`() {
        val errorMsg = "error message"

        testCoroutineRule.runBlockingTest {
            `when`(useCase.invoke()).thenReturn(
                flowOf(Resource.Error(errorMsg))
            )
        }
        testCoroutineRule.pauseDispatcher()

        val viewModel = MovieViewModel(useCase)

        assertThat(viewModel.stateFlow.value, `is`(Resource.Loading))

        testCoroutineRule.resumeDispatcher()

        assertThat(viewModel.stateFlow.value, `is`(Resource.Error(errorMsg)))
    }
}
package com.sample.movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.movies.domain.Movie
import com.sample.movies.usecase.GetMoviesUseCase
import com.sample.movies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<Resource<List<Movie>>>(Resource.Loading)
    val stateFlow: StateFlow<Resource<List<Movie>>>
        get() = _stateFlow

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            getMoviesUseCase.invoke().collect {
                _stateFlow.tryEmit(it)
            }
        }
    }
}
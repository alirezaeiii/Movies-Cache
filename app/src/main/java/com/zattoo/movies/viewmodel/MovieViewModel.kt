package com.zattoo.movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zattoo.movies.data.home.Movie
import com.zattoo.movies.usecase.GetMoviesUseCase
import com.zattoo.movies.utils.Resource
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
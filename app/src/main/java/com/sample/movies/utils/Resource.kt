package com.sample.movies.utils

sealed class Resource<out R> {
    object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T?, val hideRefreshing: Boolean = false) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}
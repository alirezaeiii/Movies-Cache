package com.sample.movies.data.network

import com.sample.movies.data.response.MovieListOffers
import com.sample.movies.data.response.MovieListResponse
import retrofit2.http.GET

interface MovieService {
    @GET("movie_offers.json")
    suspend fun fetchMovieListOffers(): MovieListOffers

    @GET("movie_data.json")
    suspend fun fetchMovieList(): MovieListResponse
}
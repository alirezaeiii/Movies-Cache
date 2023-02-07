package com.sample.movies.data

import retrofit2.http.GET

interface MovieService {
    @GET("movie_offers.json")
    suspend fun fetchMovieListOffers(): MovieListOffers

    @GET("movie_data.json")
    suspend fun fetchMovieList(): MovieListResponse
}
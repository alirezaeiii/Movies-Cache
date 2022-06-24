package com.zattoo.movies.utils

import com.zattoo.movies.data.MovieListEntity
import com.zattoo.movies.data.MovieListOffers
import com.zattoo.movies.data.MovieService
import com.zattoo.movies.data.home.Currency
import com.zattoo.movies.data.home.Image
import com.zattoo.movies.data.home.Movie
import com.zattoo.movies.data.home.Price
import kotlinx.coroutines.runBlocking

fun fetchMovieList(service: MovieService) = runBlocking {
    service.fetchMovieList()
}

fun fetchMovieListOffers(service: MovieService) = runBlocking {
    service.fetchMovieListOffers()
}

fun createMovies(
    movieDetails: List<MovieListEntity.MovieData>,
    movieListOffers: MovieListOffers
): List<Movie> {
    return movieListOffers.offers.mapNotNull { offers ->
        val details = movieDetails.find { it.movie_id == offers.movie_id }
        details?.let {
            val movieOfferPrice = offers.price
            val currency = Currency(movieOfferPrice.last().toString())
            val price = movieOfferPrice.substring(0 until movieOfferPrice.length - 1).toFloat()
            Movie(
                id = it.movie_id,
                title = it.title,
                subtitle = it.sub_title,
                price = Price(price, currency),
                image = Image(movieListOffers.image_base + offers.image),
                availability = offers.available
            )
        }
    }
}
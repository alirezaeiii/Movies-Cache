package com.sample.movies.utils

import com.sample.movies.data.response.MovieListOffers
import com.sample.movies.data.response.MovieListResponse
import com.sample.movies.domain.Currency
import com.sample.movies.domain.Image
import com.sample.movies.domain.Movie
import com.sample.movies.domain.Price

fun createMovies(
    movieDetails: List<MovieListResponse.MovieData>,
    movieListOffers: MovieListOffers
): List<Movie> {
    return movieListOffers.offers.mapNotNull { offers ->
        val details = movieDetails.find { it.movieId == offers.movieId }
        details?.let {
            val movieOfferPrice = offers.price
            val currency = Currency(movieOfferPrice.last().toString())
            val price = movieOfferPrice.substring(0 until movieOfferPrice.length - 1).toFloat()
            Movie(
                id = it.movieId,
                title = it.title,
                subtitle = it.subTitle,
                price = Price(price, currency),
                image = Image(movieListOffers.imageBase + offers.image),
                availability = offers.available
            )
        }
    }
}
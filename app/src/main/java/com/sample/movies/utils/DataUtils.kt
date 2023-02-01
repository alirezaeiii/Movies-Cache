package com.sample.movies.utils

import com.sample.movies.data.*
import com.sample.movies.domain.Currency
import com.sample.movies.domain.Image
import com.sample.movies.domain.Movie
import com.sample.movies.domain.Price

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
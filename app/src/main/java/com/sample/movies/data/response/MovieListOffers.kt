package com.sample.movies.data.response

import com.squareup.moshi.Json

data class MovieListOffers(
    @Json(name = "image_base")
    val imageBase: String,
    @Json(name = "movie_offers")
    val offers: List<MovieOffer>
) {
    data class MovieOffer(
        @Json(name = "available")
        val available: Boolean,
        @Json(name = "image")
        val image: String,
        @Json(name = "movie_id")
        val movieId: Int,
        @Json(name = "price")
        val price: String
    )
}

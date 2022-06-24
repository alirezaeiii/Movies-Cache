package com.zattoo.movies.data.home

data class Movie(
    val title: String,
    val subtitle: String,
    val price: Price,
    val image: Image,
    val availability: Boolean,
    val id: Int
)

data class Image(
    val url: String
)

data class Price(
    val value: Float,
    val currency: Currency
)

data class Currency(
    val symbol: String
)
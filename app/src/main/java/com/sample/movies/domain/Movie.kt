package com.sample.movies.domain

import com.sample.movies.database.MoviesEntity

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

fun List<Movie>.asDatabaseModel(): MoviesEntity = MoviesEntity(movies = map {
    MoviesEntity.MovieEntity(
        title = it.title,
        subtitle = it.subtitle,
        price = MoviesEntity.MovieEntity.PriceEntity(
            value = it.price.value,
            currency = MoviesEntity.MovieEntity.PriceEntity.CurrencyEntity(symbol = it.price.currency.symbol)
        ),
        image = MoviesEntity.MovieEntity.ImageEntity(url = it.image.url),
        availability = it.availability,
        id = it.id
    )
})

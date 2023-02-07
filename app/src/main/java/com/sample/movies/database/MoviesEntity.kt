package com.sample.movies.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sample.movies.domain.Currency
import com.sample.movies.domain.Image
import com.sample.movies.domain.Movie
import com.sample.movies.domain.Price

@Entity(tableName = "movies")
class MoviesEntity(
    @PrimaryKey val primaryKey: String = "movie",
    val movies: List<MovieEntity>
) {
    class MovieEntity(
        val title: String,
        val subtitle: String,
        @Embedded
        val price: PriceEntity,
        @Embedded
        val image: ImageEntity,
        val availability: Boolean,
        val id: Int
    ) {

        data class ImageEntity(
            val url: String
        )

        data class PriceEntity(
            val value: Float,
            @Embedded
            val currency: CurrencyEntity
        ) {
            data class CurrencyEntity(
                val symbol: String
            )
        }
    }
}

fun MoviesEntity.asDomainModel(): List<Movie> =
    this.movies.map {
        Movie(
            title = it.title,
            subtitle = it.subtitle,
            price = Price(
                value = it.price.value,
                currency = Currency(symbol = it.price.currency.symbol)
            ),
            Image(url = it.image.url),
            availability = it.availability,
            id = it.id
        )
    }
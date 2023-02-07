package com.sample.movies.data

import com.squareup.moshi.Json

data class MovieListResponse(
    @Json(name = "movie_data")
    val movieData: List<MovieData>
) {
    data class MovieData(
        @Json(name = "movie_id")
        val movieId: Int,
        @Json(name = "sub_title")
        val subTitle: String,
        @Json(name = "title")
        val title: String
    )
}

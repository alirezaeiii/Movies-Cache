package com.sample.movies.database

import androidx.room.TypeConverter
import com.google.gson.Gson

class MoviesEntityConvertor {

    @TypeConverter
    fun jsonToList(value: String): List<MoviesEntity.MovieEntity> =
        Gson().fromJson(value, Array<MoviesEntity.MovieEntity>::class.java).toList()

    @TypeConverter
    fun listToJson(value: List<MoviesEntity.MovieEntity?>): String =
        Gson().toJson(value.filterNotNull())
}
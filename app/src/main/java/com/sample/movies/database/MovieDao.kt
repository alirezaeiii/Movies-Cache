package com.sample.movies.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: MoviesEntity)

    @Query("SELECT * FROM movies")
    suspend fun getMovies(): MoviesEntity?

    @Query("DELETE FROM movies")
    suspend fun deleteMovies()
}
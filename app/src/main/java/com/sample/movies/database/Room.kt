package com.sample.movies.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MoviesEntity::class], version = 1, exportSchema = false)
@TypeConverters(MoviesEntityConvertor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
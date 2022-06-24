package com.zattoo.movies.di

import com.zattoo.movies.repository.MovieRepository
import com.zattoo.movies.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    internal abstract fun bindBrochureRepository(brochureRepository: MovieRepositoryImpl): MovieRepository
}
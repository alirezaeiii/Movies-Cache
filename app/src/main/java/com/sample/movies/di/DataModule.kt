package com.sample.movies.di

import com.sample.movies.data.network.MovieService
import com.sample.movies.ui.home.HomeAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Singleton
    @Provides
    fun provideNetworkClient(): Retrofit = createNetworkClient(BASE_URL)

    @Singleton
    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieService = retrofit.create(MovieService::class.java)

    @Singleton
    @Provides
    fun provideHomeAdapter(): HomeAdapter = HomeAdapter()
}

private const val BASE_URL = "https://movies-assessment.firebaseapp.com/"
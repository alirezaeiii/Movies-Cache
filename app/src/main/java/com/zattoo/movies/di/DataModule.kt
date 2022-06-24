package com.zattoo.movies.di

import com.zattoo.movies.data.MovieService
import com.zattoo.movies.ui.home.HomeAdapter
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
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
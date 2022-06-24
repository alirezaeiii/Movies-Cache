package com.zattoo.movies.di

import android.content.Context
import com.zattoo.movies.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppUtilsModule {
    @Singleton
    @Provides
    fun provideNetworkUtils(context: Context): NetworkUtils {
        return NetworkUtils(context)
    }
}
package com.zattoo.movies

import android.app.Application
import com.zattoo.movies.di.ApplicationComponent
import com.zattoo.movies.di.DaggerApplicationComponent
import timber.log.Timber

class MoviesApp : Application() {
    lateinit var applicationComponent: ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        setup()
    }

    private fun setup() {
        applicationComponent = DaggerApplicationComponent.builder()
            .bindContext(this)
            .build()
        Timber.plant(Timber.DebugTree())
    }
}
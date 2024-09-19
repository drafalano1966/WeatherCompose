package com.example.weathercompose

import android.app.Application
import com.example.weathercompose.di.app
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class WeatherApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@WeatherApp)
            modules(app)
        }
    }
}
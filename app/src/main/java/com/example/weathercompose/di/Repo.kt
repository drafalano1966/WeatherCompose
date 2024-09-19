package com.example.weathercompose.di

import com.example.weathercompose.data.SearchDataStore
import com.example.weathercompose.repository.WeatherRepo
import com.example.weathercompose.repository.WeatherRepoImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repo = module {
    singleOf(::WeatherRepoImpl) { bind<WeatherRepo>() }
    single { SearchDataStore(androidApplication()) }
}
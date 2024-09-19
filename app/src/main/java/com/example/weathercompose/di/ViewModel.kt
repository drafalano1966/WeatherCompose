package com.example.weathercompose.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.example.weathercompose.viewmodel.WeatherViewModel

val viewModel = module {
    viewModelOf(::WeatherViewModel)
}
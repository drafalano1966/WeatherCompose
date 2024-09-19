package com.example.weathercompose.repository

import com.example.weathercompose.viewmodel.UIState
import kotlinx.coroutines.flow.Flow

interface WeatherRepo {
    suspend fun getWeatherDetails(locationQuery: String?): Flow<UIState>
}
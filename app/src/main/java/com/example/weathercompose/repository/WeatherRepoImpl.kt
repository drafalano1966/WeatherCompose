package com.example.weathercompose.repository

import com.example.weathercompose.network.NetworkClient
import com.example.weathercompose.viewmodel.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepoImpl: WeatherRepo {
    override suspend fun getWeatherDetails(locationQuery: String?): Flow<UIState> {
        return flow {
            emit(UIState.Loading())

            val response = NetworkClient.api.getWeatherDetails(locationQuery)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(UIState.Loading(false))
                    emit(UIState.Success(it))
                } ?: kotlin.run {
                    emit(UIState.Loading(false))
                    emit(UIState.Failure(response.message()))
                }
            }
            else {
                emit(UIState.Loading(false))
                emit(UIState.Failure(response.raw().toString()))
            }
        }
    }
}
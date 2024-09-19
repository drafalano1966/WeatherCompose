package com.example.weathercompose.network

import com.example.weathercompose.data.MainDataModel
import com.example.weathercompose.network.NetworkConstants.API_ENDPOINT
import com.example.weathercompose.network.NetworkConstants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET(API_ENDPOINT)
    suspend fun getWeatherDetails(
        @Query("q") location: String? = null,    // for US cities: "Orlando,fl,us"
        @Query("APPID") appId: String = API_KEY,
    ): Response<MainDataModel>
}
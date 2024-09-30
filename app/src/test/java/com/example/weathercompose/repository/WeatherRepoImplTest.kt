package com.example.weathercompose.repository

import com.example.weathercompose.data.MainDataModel
import com.example.weathercompose.network.WeatherApi
import com.example.weathercompose.viewmodel.UIState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

annotation class MockK

class WeatherRepoImplTest {
    @MockK
    private lateinit var api: WeatherApi

    @MockK
    private lateinit var response: Response<MainDataModel>

    private lateinit var weatherRepoImpl: WeatherRepoImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        //NetworkClient.api = api
        weatherRepoImpl = WeatherRepoImpl()
    }

    @Test
    fun `getWeatherDetails should emit Loading and Success states when successful`() =
        runBlocking {
            // Given
            val locationQuery = "Bronx"
            val weatherData = MainDataModel("", mockk(),0, mockk(),0,0, mockk(), "", mockk(), mockk(),0,0, mockk(), mockk())
            every { response.isSuccessful } returns true
            every { response.body() } returns weatherData
            coEvery { api.getWeatherDetails(locationQuery) } returns response

            // When
            val flow = weatherRepoImpl.getWeatherDetails(locationQuery)

            // Then
            kotlin.test.assertEquals(UIState.Loading(), flow.first())
            kotlin.test.assertEquals(UIState.Success(weatherData), flow.last())
        }

    @Test
    fun `getWeatherDetails should emit Loading and Failure states when unsuccessful`() = runBlocking {
        // Given
        val locationQuery = "London"
        val errorMessage = "Error message"
        every { response.isSuccessful } returns false
        every { response.raw().toString() } returns errorMessage
        coEvery { api.getWeatherDetails(locationQuery) } returns response

        // When
        val flow = weatherRepoImpl.getWeatherDetails(locationQuery)

        // Then
        assertEquals(UIState.Loading(), flow.first())
        assertEquals(UIState.Failure(errorMessage), flow.last())
    }

    @kotlin.test.Test
    fun `getWeatherDetails should emit Loading and Failure states when response body is null`() = runBlocking {
        // Given
        val locationQuery = "London"
        val errorMessage = "Response.message()"
        every { response.isSuccessful } returns true
        every { response.body() } returns null
        every { response.message() } returns errorMessage
        coEvery { api.getWeatherDetails(locationQuery) } returns response

        // When
        val flow = weatherRepoImpl.getWeatherDetails(locationQuery)

        // Then
        assertEquals(UIState.Loading(), flow.first())
        assertEquals(UIState.Failure(errorMessage), flow.last())
    }
}

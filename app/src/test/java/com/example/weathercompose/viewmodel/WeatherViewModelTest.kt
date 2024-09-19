/*
package com.example.weathercompose.viewmodel

import com.example.weathercompose.data.MainDataModel
import com.example.weathercompose.data.SearchDataStore
import com.example.weathercompose.repository.WeatherRepo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import kotlin.test.Test

@ExperimentalCoroutinesApi
class WeatherViewModelTest{
    private val testDispatcher = StandardTestDispatcher()

    @RelaxedMockK
    private lateinit var repository: WeatherRepo

    @RelaxedMockK
    private lateinit var dataStore: SearchDataStore

    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = WeatherViewModel(repository, dataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchWeatherDetails should update weatherResponseState`() = runTest {
        // Given
        val searchText = "London"
        val uiState = UIState.Success(MainDataModel())
        coEvery { repository.getWeatherDetails(searchText) } returns flowOf(uiState)

        // When
        viewModel.updateSearchText(searchText)
        viewModel.fetchWeatherDetails()

        // Then
        coVerify { repository.getWeatherDetails(searchText) }
        assertEquals(uiState, viewModel.weatherResponseState.value)
    }

    @Test
    fun `updateSearchText should update searchText`() = runTest {
        // Given
        val searchText = "London"

        // When
        viewModel.updateSearchText(searchText)

        // Then
        assertEquals(searchText, viewModel.searchText.value)
    }

    @Test
    fun `init should collect searchToken and fetchWeatherDetails`() = runTest {
        // Given
        val searchToken = "London"
        every { dataStore.getSearchToken } returns flowOf(searchToken)
        coEvery { repository.getWeatherDetails(searchToken) } returns flowOf(UIState.Empty)

        // When
        viewModel = WeatherViewModel(repository, dataStore) // Re-initialize to trigger init block

        // Then
        coVerify { dataStore.getSearchToken }
        coVerify { repository.getWeatherDetails(searchToken) }
        assertEquals(searchToken, viewModel.searchText.value)
    }

    @Test
    fun `fetchWeatherDetails should updateDataStore`() = runTest {
        // Given
        val searchText = "London"
        coEvery { repository.getWeatherDetails(any()) } returns flowOf(UIState.Empty)
        coEvery { dataStore.saveToken(any()) } just runs

        // When
        viewModel.updateSearchText(searchText)
        viewModel.fetchWeatherDetails()

        // Then
        coVerify { dataStore.saveToken(searchText) }
    }
}
*/

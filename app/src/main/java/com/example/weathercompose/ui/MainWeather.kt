package com.example.weathercompose.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weathercompose.ConversionExt.toCelsius
import com.example.weathercompose.ConversionExt.toFahrenheit
import com.example.weathercompose.ConversionExt.toKilometer
import com.example.weathercompose.data.MainDataModel
import com.example.weathercompose.viewmodel.UIState
import com.example.weathercompose.viewmodel.WeatherViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DisplayWeatherDetails(viewModel: WeatherViewModel = koinViewModel()) {
    val uiState by viewModel.weatherResponseState.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    Scaffold(
        topBar = {
            SearchBar(
                query = searchText,
                onQueryChange = viewModel::updateSearchText,
                onSearch = {
                    viewModel.fetchWeatherDetails()
                },
                active = isSearching,
                onActiveChange = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White),
            ) {

            }
        }
    ) {
        when (uiState) {
            is UIState.Success -> {
                DisplaySuccess(weatherResponse = (uiState as UIState.Success).data, padding = it)
            }
            UIState.Empty -> DisplayError(error = "Unknown Error")
            is UIState.Failure -> DisplayError(error = "Error retrieving data")
            is UIState.Loading -> CircularProgressIndicator(
                Modifier.fillMaxSize()
                    .padding(top = 120.dp, start = 32.dp)
            )
        }
    }
}

@Composable
private fun DisplayError(error: String) {
    val context = LocalContext.current
    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
}

@Composable
private fun DisplaySuccess(weatherResponse: MainDataModel, padding: PaddingValues) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = padding.calculateTopPadding())
    ) {
        Text(
            text = "City Name: ${weatherResponse.name} ",
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
        )
        Row {
            Text(
                text = "Latitude ${weatherResponse.coord?.lat ?: "unknown"} ",
                color = Color.Black,
                fontSize = 14.sp,
            )
            Text(
                text = "Longitude ${weatherResponse.coord?.lon ?: "unknown"} ",
                color = Color.Black,
                fontSize = 14.sp,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AsyncImage(
                model = getImageUrl(weatherResponse.weather?.first()?.icon),
                contentDescription = "UI_Image_Weather_Icon",
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Magenta)
            )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "${weatherResponse.main?.temp?.toInt()}K", fontSize = 20.sp)
                Text(
                    text = "${weatherResponse.main?.temp?.toCelsius()?.toInt()}\u00B0C",
                    fontSize = 20.sp
                )
                Text(
                    text = "${weatherResponse.main?.temp?.toFahrenheit()?.toInt()}\u00B0F",
                    fontSize = 20.sp
                )
            }
        }
        Row {
            Text(
                text = "Feels like ${
                    weatherResponse.main?.feelsLike?.toCelsius()?.toInt()
                }°C. "
            )
            Text(
                text = "${
                    weatherResponse.weather?.first()?.description.toString().replaceFirstChar {
                        it.uppercase()
                    }
                }."
            )
        }
        Row {
            Text(
                text = "Pressure: ${weatherResponse.main?.pressure} hPa",
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Humidity: ${weatherResponse.main?.humidity}%",
                modifier = Modifier.padding(8.dp)
            )
        }
        Text(text = "Visibility: ${weatherResponse.visibility?.toKilometer().toString()}")
    }
}

private fun getImageUrl(iconCode: String?): String =
    StringBuilder("https://openweathermap.org/img/wn/")
        .append(iconCode)
        .append("@4x.png")
        .toString()


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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
                    .padding(16.dp,16.dp,16.dp,16.dp)
                    .background(Color(0xFF00B2CA)),
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
                Modifier
                    .fillMaxSize()
                    .padding(top = 160.dp, start = 32.dp)
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
            .background(Color(0xFF00B2CA))
            .padding(top = padding.calculateTopPadding()),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "City Name: ${weatherResponse.name} ",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
        )
        Row(
            modifier = Modifier
                .background(Color.White)
        ) {
            Text(
                text = "Latitude ${weatherResponse.coord?.lat ?: "unknown"} ",
                color = Color.Black,
                fontSize = 12.sp,
                modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 0.dp)
            )
            Text(
                text = "Longitude ${weatherResponse.coord?.lon ?: "unknown"} ",
                color = Color.Black,
                fontSize = 12.sp,
                modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 0.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .padding(start = 16.dp, top = 5.dp, end = 16.dp, bottom = 5.dp)
                .background(color = Color(0xFF1D4E89), shape = RoundedCornerShape(20.dp))
        ) {
            AsyncImage(
                model = getImageUrl(weatherResponse.weather?.first()?.icon),
                contentDescription = "UI_Image_Weather_Icon",
                modifier = Modifier
                    .size(120.dp)
                    .background(Color(0Xfff79256),shape = RoundedCornerShape(20.dp))
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(0.dp,0.dp,180.dp,0.dp),
                    text = "${weatherResponse.main?.temp?.toInt()}K",
                    fontSize = 15.sp,
                    color = Color.White
                )
                Text(
                    text = "${weatherResponse.main?.temp?.toCelsius()?.toInt()}\u00B0C",
                    fontSize = 15.sp,
                    color = Color.White
                )
                Text(
                    text = "${weatherResponse.main?.temp?.toFahrenheit()?.toInt()}\u00B0F",
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .padding(start = 16.dp, top = 5.dp, end = 16.dp, bottom = 5.dp)
                .background(color = Color(0xFF1D4E89), shape = RoundedCornerShape(20.dp))
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                color = Color.White,
                text = "Feels like ${
                    weatherResponse.main?.feelsLike?.toCelsius()?.toInt()
                }°C. "
            )
            Text(
                color = Color.White,
                text = "${
                    weatherResponse.weather?.first()?.description.toString().replaceFirstChar {
                        it.uppercase()
                    }
                }."
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .padding(start = 16.dp, top = 5.dp, end = 16.dp, bottom = 5.dp)
                .background(color = Color(0xFF1D4E89), shape = RoundedCornerShape(20.dp))
        ) {
            Text(
                text = "Pressure: ${weatherResponse.main?.pressure} hPa",
                modifier = Modifier.padding(8.dp),
                color = Color.White
            )
            Text(
                text = "Humidity: ${weatherResponse.main?.humidity}%",
                modifier = Modifier.padding(8.dp),
                color = Color.White
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .padding(start = 16.dp, top = 5.dp, end = 16.dp, bottom = 15.dp)
                .background(color = Color(0xFF1D4E89), shape = RoundedCornerShape(20.dp))
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                color = Color.White,
                text = "Visibility: ${weatherResponse.visibility?.toKilometer().toString()}"
            )
        }
    }
}

private fun getImageUrl(iconCode: String?): String =
    StringBuilder("https://openweathermap.org/img/wn/")
        .append(iconCode)
        .append("@4x.png")
        .toString()

package com.example.travelcompanion.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelcompanion.data.Datasource
import com.example.travelcompanion.models.WeatherUiState
import com.example.travelcompanion.models.WeatherViewModel
import kotlin.math.floor


@Composable
fun WeatherScreen(modifier: Modifier = Modifier) {
    val weatherVM = viewModel<WeatherViewModel>()
    val weatherUiState = weatherVM.weatherUIState
    val weatherIcons = Datasource().loadIcons()

    when (weatherUiState) {
        is WeatherUiState.Success -> {

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Approximate Time of Arrival: 12:30 PM EST")
                Text("Weather Conditions Upon Arrival", fontSize = 20.sp)

                val index = weatherIcons.indexOfFirst {
                    it.iconID == weatherUiState.weather.data[0].weather.icon
                }

                if (weatherUiState.weather.state_code[0].isDigit()) {
                    Text(
                        weatherUiState.weather.city_name + ", " + weatherUiState.weather.country_code,
                        fontSize = 36.sp
                    )
                } else {
                    Text(
                        weatherUiState.weather.city_name + ", " + weatherUiState.weather.state_code,
                        fontSize = 36.sp
                    )
                    Text(weatherUiState.weather.country_code)
                }
                Text(
                    floor(weatherUiState.weather.data[0].temp).toInt().toString() + "\u2109",
                    fontSize = 48.sp
                )
                Text(
                    "Feels like: " + floor(weatherUiState.weather.data[0].app_temp).toInt()
                        .toString() + "\u2109"
                )

                Image(
                    painterResource(id = weatherIcons[index].iconImage),
                    contentDescription = null,
                    modifier = modifier.size(150.dp),
                )
                Text(weatherUiState.weather.data[0].weather.description)
                Text("Precipitation Chance: " + weatherUiState.weather.data[0].pop.toString() + "%")
                Spacer(modifier = modifier.height(16.dp))
                Text("Items to Bring: ")
                Text("  \u25CF    Item 1")
                Text("  \u25CF    Item 2")
                Text("  \u25CF    Item 3")
                Text("  \u25CF    Item 4")
                Spacer(modifier = modifier.height(8.dp))
                Button(onClick = { /*TODO*/ }) {
                    Text("Let's Go!")
                }

            }
        }

        is WeatherUiState.Error -> {
            Text("Error")
        }

        is WeatherUiState.Loading -> {
            Text("Loading")
        }
    }
}


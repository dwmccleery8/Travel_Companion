package com.example.travelcompanion.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelcompanion.models.WeatherUiState
import com.example.travelcompanion.models.WeatherViewModel

@Composable
fun WeatherScreen(modifier: Modifier = Modifier) {
    val weatherVM = viewModel<WeatherViewModel>()
    val weatherUiState = weatherVM.weatherUIState

    when (weatherUiState) {
        is WeatherUiState.Success -> {

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Weather Result", fontSize = 20.sp)

                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(modifier = modifier.padding(8.dp)) {
                        Text("code: " + weatherUiState.weather.data)

                    }

                }
            }
        }
        is WeatherUiState.Error ->{
            Text("Error")
        }
        is WeatherUiState.Loading -> {
            Text("Loading")
        }
    }

}

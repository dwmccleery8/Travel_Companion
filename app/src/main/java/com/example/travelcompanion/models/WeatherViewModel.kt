package com.example.travelcompanion.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelcompanion.apis.WeatherAPI
import com.example.travelcompanion.WeatherService
import kotlinx.coroutines.launch

sealed interface WeatherUiState {
    class Success(val weather: WeatherService) : WeatherUiState
    object Error : WeatherUiState
    object Loading : WeatherUiState
}

class WeatherViewModel : ViewModel() {

    var weatherUIState: WeatherUiState by mutableStateOf(WeatherUiState.Loading)


    init {
        getWeatherData()
    }

    fun getWeatherData() {
        viewModelScope.launch {

            try {
                val weather = WeatherAPI.retrofitService.getWeather(lon = 70.0, lat = 70.0)
                weatherUIState = WeatherUiState.Success(weather = weather)
            } catch (e: Exception) {
                println("IO Error ${e.message}")
                weatherUIState = WeatherUiState.Error
            }
        }
    }
}




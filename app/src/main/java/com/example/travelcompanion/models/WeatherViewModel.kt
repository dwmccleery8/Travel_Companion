package com.example.travelcompanion.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelcompanion.apis.WeatherAPI
import com.example.travelcompanion.WeatherServiceDaily
import com.example.travelcompanion.WeatherServiceHourly
import kotlinx.coroutines.launch

sealed interface WeatherUiState {
    class Success(val weatherHourly: WeatherServiceHourly, val weatherDaily: WeatherServiceDaily) : WeatherUiState
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
                val weatherHourly = WeatherAPI.retrofitService.getWeatherHourly(lon = -80.08037698997848, lat = 41.155372314696706)
                val weatherDaily = WeatherAPI.retrofitService.getWeatherDaily(lon = -80.08037698997848, lat = 41.155372314696706)
                weatherUIState = WeatherUiState.Success(weatherHourly = weatherHourly, weatherDaily = weatherDaily)
            } catch (e: Exception) {
                println("IO Error ${e.message}")
                weatherUIState = WeatherUiState.Error
            }
        }
    }
}

fun getBetterDate(date:String): String {
    println(date)
    var betterDate = ""
    betterDate += if (date[5] == '0') {
        "Jan "
    }
    else {
        "Dec "
    }
    if (date[8] != '0') {
        betterDate += date[8]
    }
    betterDate += date[9]

    return betterDate
}



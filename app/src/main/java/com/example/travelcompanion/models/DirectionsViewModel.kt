package com.example.travelcompanion.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelcompanion.apis.DirectionsAPI
import kotlinx.coroutines.launch

sealed interface DirectionsUiState {
    class Success(val directions: DirectionsObjects) : DirectionsUiState
    object Error : DirectionsUiState
    object Loading : DirectionsUiState
}

class DirectionsViewModel : ViewModel() {

    var directionsUIState: DirectionsUiState by mutableStateOf(DirectionsUiState.Loading)
    var startingLat by mutableDoubleStateOf(0.0)
    var startingLon by mutableDoubleStateOf(0.0)
    var destinationLat by mutableDoubleStateOf(0.0)
    var destinationLon by mutableDoubleStateOf(0.0)
    var destinationString by mutableStateOf("")

    init {
        // getDirectionsData()
    }

    fun reset() {
        directionsUIState = DirectionsUiState.Loading
        startingLat = 0.0
        startingLon = 0.0
        destinationLat = 0.0
        destinationLon = 0.0
        destinationString = ""
    }


    fun getDirectionsData() {
        viewModelScope.launch {

            try {
                val directions = DirectionsAPI.retrofitService.getDirections(destination =
                ("$destinationLat,$destinationLon"), origin = ("$startingLat,$startingLon"), departure_time = 1703414366)
                directionsUIState = DirectionsUiState.Success(directions = directions)
            } catch (e: Exception) {
                println("IO Error ${e.message}")
                directionsUIState = DirectionsUiState.Error
            }
        }
    }
}
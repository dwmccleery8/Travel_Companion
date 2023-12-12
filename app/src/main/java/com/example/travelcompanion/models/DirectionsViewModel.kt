package com.example.travelcompanion.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelcompanion.apis.DirectionsAPI
import kotlinx.coroutines.launch

sealed interface DirectionsUiState {
    class Success(val directions: DirectionsRoute) : DirectionsUiState

    object Error : DirectionsUiState

    object Loading : DirectionsUiState
}

class DirectionsViewModel : ViewModel() {

    var directionsUIState: DirectionsUiState by mutableStateOf(DirectionsUiState.Loading)

    init {
        getDirectionsData()
    }

    fun getDirectionsData() {
        viewModelScope.launch {

            try {
                val directions = DirectionsAPI.retrofitService.getDirections(destination = "", origin = "", departure_time = 0)
                directionsUIState = DirectionsUiState.Success(directions = directions)
            } catch (e: Exception) {
                println("IO Error ${e.message}")
                directionsUIState = DirectionsUiState.Error
            }
        }
    }
}
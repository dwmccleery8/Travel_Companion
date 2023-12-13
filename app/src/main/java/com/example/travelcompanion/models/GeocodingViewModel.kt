package com.example.travelcompanion.models


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelcompanion.apis.GeocodingAPI
import kotlinx.coroutines.launch

sealed interface GeocodingUiState {
    class Success(val address: AddressResult) : GeocodingUiState

    object Error : GeocodingUiState

    object Loading : GeocodingUiState
}

class GeocodingViewModel : ViewModel() {

    var geocodingUiState: GeocodingUiState by mutableStateOf(GeocodingUiState.Loading)
    var OriginAddressText by mutableStateOf("origin")
    var DestinationAddressText by mutableStateOf("destination")
    var isVMOrigin by mutableStateOf(true)

    init {
        getGeocodingData()
    }

    fun getGeocodingData() {
        viewModelScope.launch {

            try {
                if (isVMOrigin) {
                    val address = GeocodingAPI.retrofitService.getAddress(text = OriginAddressText)
                    geocodingUiState = GeocodingUiState.Success(address = address)

                } else {
                    val address = GeocodingAPI.retrofitService.getAddress(text = DestinationAddressText)
                    geocodingUiState = GeocodingUiState.Success(address = address)
                }

            } catch (e: Exception) {
                println("IO Error ${e.message}")
                geocodingUiState = GeocodingUiState.Error
            }
        }
    }
}
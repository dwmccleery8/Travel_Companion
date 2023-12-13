package com.example.travelcompanion.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelcompanion.R
import com.example.travelcompanion.models.DirectionsViewModel
import com.example.travelcompanion.models.GeocodingUiState
import com.example.travelcompanion.models.GeocodingViewModel
import com.example.travelcompanion.models.ResultAddress
import com.example.travelcompanion.models.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddressAutoCompleteScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
    directionsVM: DirectionsViewModel,
    weatherVM: WeatherViewModel,
    addressVM: GeocodingViewModel
) {
    Image(
        painter = painterResource(id = R.drawable.app_background),
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = 0.5f
    )
    val addressUiState = addressVM.geocodingUiState
    val showOriginResults = remember { mutableStateOf(false) }
    val showDestinationResults = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current


    when (addressUiState) {
        is GeocodingUiState.Success -> {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(bottom = 150.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Enter Starting Location", fontSize = 24.sp)
                    Spacer(modifier = modifier.height(4.dp))

                    SearchBar(
                        query = addressVM.OriginAddressText,
                        onQueryChange = {
                            addressVM.OriginAddressText = it
                            if (addressVM.OriginAddressText != "") {
                                addressVM.getGeocodingData()
                                showOriginResults.value = true
                                addressVM.isVMOrigin = true
                            }
                        },
                        onSearch = { keyboardController?.hide() },
                        active = true,
                        onActiveChange = {},
                        colors = SearchBarDefaults.colors(Color.LightGray),
                        modifier = modifier.height(72.dp)
                    ) {

                    }
                    Spacer(modifier = modifier.height(12.dp))

                    Text("Enter Destination", fontSize = 24.sp)
                    Spacer(modifier = modifier.height(4.dp))
                    SearchBar(
                        query = addressVM.DestinationAddressText,
                        onQueryChange = {
                            addressVM.DestinationAddressText = it
                            if (addressVM.DestinationAddressText != "") {
                                addressVM.getGeocodingData()
                                showDestinationResults.value = true
                                addressVM.isVMOrigin = false
                            }
                        },
                        onSearch = { keyboardController?.hide() },
                        active = true,
                        onActiveChange = {},
                        colors = SearchBarDefaults.colors(Color.LightGray),
                        modifier = modifier.height(72.dp)
                    ) {

                    }
                    Spacer(modifier = modifier.height(12.dp))
                    Button(
                        onClick = {
                            onNext()
                            directionsVM.getDirectionsData()
                            weatherVM.getWeatherData()
                        },
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Text("Go to Results Screen")
                    }

                }

            }

            if (showOriginResults.value) {
                LazyColumn(
                    modifier = modifier
                        .padding(top = 270.dp)
                        .fillMaxWidth()
                ) {
                    itemsIndexed(addressUiState.address.results) { index, result ->
                        val backGroundColor = if (index % 2 == 0) {
                            Color(0xFFBCAAA4)
                        } else {
                            Color(0xFFA1887F)
                        }
                        ResultCard(
                            result = result, modifier,
                            showOriginResults, addressVM, backGroundColor, directionsVM, weatherVM
                        )
                    }
                }
            }
            if (showDestinationResults.value) {
                LazyColumn(
                    modifier = modifier
                        .padding(top = 390.dp)
                        .fillMaxWidth()
                ) {
                    itemsIndexed(addressUiState.address.results) { index, result ->
                        val backGroundColor = if (index % 2 == 0) {
                            Color(0xFFBCAAA4)
                        } else {
                            Color(0xFFA1887F)
                        }
                        ResultCard(
                            result = result,
                            modifier,
                            showDestinationResults,
                            addressVM,
                            backGroundColor,
                            directionsVM,
                            weatherVM
                        )
                    }
                }
            }


        }

        is GeocodingUiState.Error -> {
            Text("Error")
        }

        is GeocodingUiState.Loading -> {
            Text("Loading")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultCard(
    result: ResultAddress,
    modifier: Modifier,
    showResults: MutableState<Boolean>,
    addressVM: GeocodingViewModel,
    backGroundColor: Color,
    directionsVM: DirectionsViewModel,
    weatherVM: WeatherViewModel
) {

    Card(modifier = modifier
        .height(50.dp)
        .wrapContentHeight(align = Alignment.CenterVertically)
        .fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = backGroundColor
        ),
        shape = RectangleShape,
        onClick = {
            if (addressVM.isVMOrigin) {
                directionsVM.startingLat = result.lat
                directionsVM.startingLon = result.lon
                addressVM.OriginAddressText = result.formatted
            } else {
                directionsVM.destinationLat = result.lat
                directionsVM.destinationLon = result.lon
                weatherVM.weatherLat = result.lat
                weatherVM.weatherLon = result.lon
                addressVM.DestinationAddressText = result.formatted
            }
            showResults.value = false
        }) {
        Text(result.formatted)
    }
}


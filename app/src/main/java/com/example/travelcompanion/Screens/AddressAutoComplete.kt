package com.example.travelcompanion.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelcompanion.models.GeocodingUiState
import com.example.travelcompanion.models.GeocodingViewModel
import com.example.travelcompanion.models.ResultAddress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressAutoCompleteScreen(
    modifier: Modifier = Modifier,
) {
    val addressVM = viewModel<GeocodingViewModel>()
    val addressUiState = addressVM.geocodingUiState
    val showOriginResults = remember { mutableStateOf(false)}
    val showDestinationResults = remember { mutableStateOf(false)}
    val isOrigin = remember { mutableStateOf(true)}

    when (addressUiState) {
        is GeocodingUiState.Success -> {
            Column (
                modifier = modifier.fillMaxWidth()
            ){
                Text("Enter Starting Location")
                SearchBar(
                    query = addressVM.OriginAddressText,
                    onQueryChange = {addressVM.OriginAddressText = it
                        if (addressVM.OriginAddressText != "") {
                            addressVM.getGeocodingData()
                            showOriginResults.value = true
                            addressVM.isVMOrigin = true
                        }},
                    onSearch = { addressVM.getGeocodingData()
                        showOriginResults.value = true },
                    active = true,
                    onActiveChange = {},
                    colors = SearchBarDefaults.colors(Color.LightGray),
                    modifier = modifier.height(72.dp)
                ) {

                }
                Text("Enter Destination")
                SearchBar(
                    query = addressVM.DestinationAddressText,
                    onQueryChange = {addressVM.DestinationAddressText = it
                        if (addressVM.DestinationAddressText != "") {
                            addressVM.getGeocodingData()
                            showDestinationResults.value = true
                            addressVM.isVMOrigin = false
                        }},
                    onSearch = { addressVM.getGeocodingData()
                        showDestinationResults.value = true },
                    active = true,
                    onActiveChange = {},
                    colors = SearchBarDefaults.colors(Color.LightGray),
                    modifier = modifier.height(72.dp)
                ) {

                }

            }

            if (showOriginResults.value) {
                LazyColumn(modifier = modifier
                    .padding(top = 95.dp)
                    .fillMaxWidth()) {
                    itemsIndexed(addressUiState.address.results) {index, result ->
                        var backGroundColor = if (index % 2 == 0) {
                            Color(0xFFBCAAA4)
                        } else {
                            Color(0xFFA1887F)
                        }
                        resultCard(result = result, modifier, showOriginResults, addressVM, backGroundColor, isOrigin.value)
                    }
                }
            }
            if (showDestinationResults.value) {
                LazyColumn(modifier = modifier
                    .padding(top = 180.dp)
                    .fillMaxWidth()) {
                    itemsIndexed(addressUiState.address.results) {index, result ->
                        var backGroundColor = if (index % 2 == 0) {
                            Color(0xFFBCAAA4)
                        } else {
                            Color(0xFFA1887F)
                        }
                        resultCard(result = result, modifier, showDestinationResults, addressVM, backGroundColor, isOrigin.value)
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
fun resultCard(
    result: ResultAddress,
    modifier: Modifier,
    showResults: MutableState<Boolean>,
    addressVM : GeocodingViewModel,
    backGroundColor: Color,
    isOrigin: Boolean
) {
    var startingLat = 0.0
    var startingLon = 0.0
    var destinationLat = 0.0
    var destinationLon = 0.0
    Card(modifier = modifier
        .height(50.dp)
        .wrapContentHeight(align = Alignment.CenterVertically)
        .fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = backGroundColor
        ),
        shape = RectangleShape,
        onClick = {
            if (isOrigin) {
                startingLat = result.lat
                startingLon = result.lon
                addressVM.OriginAddressText = result.formatted
            } else {
                destinationLat = result.lat
                destinationLon = result.lon
                addressVM.DestinationAddressText = result.formatted
            }
        showResults.value = false
        }) {
        Text(result.formatted, modifier = modifier)
        println(startingLat + startingLon + destinationLat + destinationLon)
    }
}


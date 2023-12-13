package com.example.travelcompanion.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelcompanion.R
import com.example.travelcompanion.WeatherDetailsDaily
import com.example.travelcompanion.data.Datasource
import com.example.travelcompanion.data.WeatherIcon
import com.example.travelcompanion.models.DirectionsUiState
import com.example.travelcompanion.models.DirectionsViewModel
import com.example.travelcompanion.models.OpenAiState
import com.example.travelcompanion.models.OpenAiVM
import com.example.travelcompanion.models.WeatherUiState
import com.example.travelcompanion.models.WeatherViewModel
import com.example.travelcompanion.models.getBetterDate
import kotlin.math.floor


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
    openAiVM: OpenAiVM,
    directionsVM: DirectionsViewModel
) {
    Image(
        painter = painterResource(id = R.drawable.app_background),
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = 0.5f
    )

    val weatherVM = viewModel<WeatherViewModel>()
    val weatherUiState = weatherVM.weatherUIState
    val weatherIcons = Datasource().loadIcons()
    val isHourly = false
    val directionsUiState = directionsVM.directionsUIState
    var durationText = ""


    when (weatherUiState) {
        is WeatherUiState.Success -> {
            val index = weatherIcons.indexOfFirst {
                if (isHourly) {
                    it.iconID == weatherUiState.weatherHourly.data[0].weather.icon

                } else {
                    it.iconID == weatherUiState.weatherDaily.data[0].weather.icon

                }
            }

            when(directionsUiState) {
                is DirectionsUiState.Success -> {
                    durationText = directionsUiState.directions.routes[0].legs[0].duration.text
                }
                is DirectionsUiState.Error -> {
                    Text("Error")
                }

                is DirectionsUiState.Loading -> {
                    Text("Loading")
                }
            }


            if (isHourly) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Travel Time: $durationText")
                    Text("Approximate Time of Arrival: 12:30 PM EST")
                    Text("Weather Conditions Upon Arrival", fontSize = 20.sp)

                    if (weatherUiState.weatherHourly.state_code[0].isDigit()) {
                        Text(
                            weatherUiState.weatherHourly.city_name + ", " + weatherUiState.weatherHourly.country_code,
                            fontSize = 36.sp
                        )
                    } else {
                        Text(
                            weatherUiState.weatherHourly.city_name + ", " + weatherUiState.weatherHourly.state_code,
                            fontSize = 36.sp
                        )
                        Text(weatherUiState.weatherHourly.country_code)
                    }
                    Text(
                        floor(weatherUiState.weatherHourly.data[0].temp).toInt()
                            .toString() + "\u2109",
                        fontSize = 48.sp
                    )
                    Text(
                        "Feels like: " + floor(weatherUiState.weatherHourly.data[0].app_temp).toInt()
                            .toString() + "\u2109"
                    )

                    Image(
                        painterResource(id = weatherIcons[index].iconImage),
                        contentDescription = null,
                        modifier = modifier.size(150.dp),
                    )
                    Text(weatherUiState.weatherHourly.data[0].weather.description)
                    Text("Precipitation Chance: " + weatherUiState.weatherHourly.data[0].pop.toString() + "%")
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
                    Button(
                        onClick = onNext
                    ) {
                        Text("Go Back to Selection Screen")
                    }

                }
            } else {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Travel Time: $durationText")
                    Text("Approximate Time of Arrival: 12:30 PM EST")
                    Text("Weather Conditions on Day of Arrival", fontSize = 20.sp)

                    if (weatherUiState.weatherDaily.state_code[0].isDigit()) {
                        Text(
                            weatherUiState.weatherDaily.city_name + ", " + weatherUiState.weatherDaily.country_code,
                            fontSize = 36.sp
                        )
                    } else {
                        Text(
                            weatherUiState.weatherDaily.city_name + ", " + weatherUiState.weatherDaily.state_code,
                            fontSize = 36.sp
                        )
                        Text(weatherUiState.weatherDaily.country_code)
                    }
                    Text("Today, " + getBetterDate(weatherUiState.weatherDaily.data[0].valid_date))

                    Text(
                        floor(weatherUiState.weatherDaily.data[0].max_temp).toInt()
                            .toString() + "\u2109 / "
                                + floor(weatherUiState.weatherDaily.data[0].min_temp).toInt()
                            .toString() + "\u2109",
                        fontSize = 48.sp
                    )

                    Image(
                        painterResource(id = weatherIcons[index].iconImage),
                        contentDescription = null,
                        modifier = modifier.size(150.dp),
                    )
                    Text(weatherUiState.weatherDaily.data[0].weather.description)
                    Row {
                        Image(
                            painterResource(id = R.drawable.rain),
                            modifier = modifier.size(24.dp),
                            contentDescription = null
                        )
                        Text("     " + weatherUiState.weatherDaily.data[0].pop.toString() + "%")
                    }
                    Text("Weather Forecast for Duration of Trip", fontSize = 18.sp)
                    LazyRow(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(4.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(weatherUiState.weatherDaily.data) { day ->
                            WeatherCard(dayData = day, modifier, weatherIcons)
                        }
                    }

                    Spacer(modifier = modifier.height(16.dp))
//                    Text("Items to Bring: ")
                    passVars(openAiVM)
                    openAiVM.getAnalysis()
                    when (val openAiUIState = openAiVM.openAiState) {
                        is OpenAiState.Success -> {
                            Text("Items to Bring: \n" + openAiUIState.summary, fontSize = 20.sp)
                        }

                        is OpenAiState.Loading -> {
                            Text("Loading Travel Analysis", fontSize = 30.sp)
                        }

                        is OpenAiState.Error -> {
                            Text("Service Error", fontSize = 30.sp)
                        }
                    }
//                    Spacer(modifier = Modifier.height(10.dp))
//                    Text("  \u25CF    Item 1")
//                    Text("  \u25CF    Item 2")
//                    Text("  \u25CF    Item 3")
//                    Text("  \u25CF    Item 4")
                    Spacer(modifier = modifier.height(8.dp))
                    Button(onClick = { /*TODO*/ }) {
                        Text("Let's Go!")
                    }
                    Button(
                        onClick = onNext
                    ) {
                        Text("Go Back to Selection Screen")
                    }

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

@Composable
fun WeatherCard(
    dayData: WeatherDetailsDaily,
    modifier: Modifier = Modifier,
    weatherIcons: List<WeatherIcon>
) {

    val index = weatherIcons.indexOfFirst {
        it.iconID == dayData.weather.icon
    }

    Card(
        modifier = modifier
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painterResource(id = weatherIcons[index].iconImage), contentDescription = null)
            Text(
                floor(dayData.max_temp).toInt()
                    .toString() + "\u2109 / "
                        + floor(dayData.min_temp).toInt()
                    .toString() + "\u2109"
            )
            Row {
                Image(
                    painterResource(id = R.drawable.rain),
                    modifier = modifier.size(24.dp),
                    contentDescription = null
                )
                Text(" " + dayData.pop + "%")
            }
            Text(getBetterDate(dayData.valid_date))

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun passVars(openAiVM: OpenAiVM) {
    openAiVM.absoluteLow = 20.0
    openAiVM.absoluteHigh = 30.0
}
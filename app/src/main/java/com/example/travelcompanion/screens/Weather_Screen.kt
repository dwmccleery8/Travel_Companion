package com.example.travelcompanion.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelcompanion.R
import com.example.travelcompanion.WeatherDetailsDaily
import com.example.travelcompanion.data.Datasource
import com.example.travelcompanion.data.WeatherIcon
import com.example.travelcompanion.models.DirectionsUiState
import com.example.travelcompanion.models.DirectionsViewModel
import com.example.travelcompanion.models.GeocodingViewModel
import com.example.travelcompanion.models.OpenAiState
import com.example.travelcompanion.models.OpenAiVM
import com.example.travelcompanion.models.WeatherUiState
import com.example.travelcompanion.models.WeatherViewModel
import com.example.travelcompanion.models.getBetterDate
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import kotlin.math.floor


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
    openAiVM: OpenAiVM,
    directionsVM: DirectionsViewModel,
    weatherVM: WeatherViewModel,
    intentOnClick: (lat: Double, long: Double) -> Unit,
    addressVM: GeocodingViewModel

) {
    Image(
        painter = painterResource(id = R.drawable.app_background),
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = 0.5f
    )

    val weatherUiState = weatherVM.weatherUIState
    val weatherIcons = Datasource().loadIcons()
    val isHourly = openAiVM.isHourly
    val directionsUiState = directionsVM.directionsUIState
    var durationText = ""
    val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
    val arrivalTime = openAiVM.departEpochTime?.plusMillis(
        60000 * convertDuration(
            durationText,
            openAiVM
        )
    )?.epochSecond

    val currentTime = System.currentTimeMillis() / 1000L
    val howManyHours = (arrivalTime?.minus(currentTime))?.div(3600)
    val hoursInt = howManyHours?.toInt()

    val localDateTime: Long = LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.ofHours(-5))
    val departureDate = openAiVM.departEpochTime?.epochSecond
    val daysDifferenceDeparture = (departureDate?.minus(localDateTime))?.div(86400)?.toInt()
    val returnDate = openAiVM.returnEpochTime?.epochSecond
    val daysDifferenceArrival = (returnDate?.minus(localDateTime))?.div(86400)?.toInt()


    when (weatherUiState) {
        is WeatherUiState.Success -> {
            val index = weatherIcons.indexOfFirst {
                if (isHourly) {
                    it.iconID == weatherUiState.weatherHourly.data[hoursInt!!].weather.icon

                } else {
                    it.iconID == weatherUiState.weatherDaily.data[daysDifferenceDeparture!!].weather.icon

                }
            }

            when (directionsUiState) {
                is DirectionsUiState.Success -> {
                    durationText = if (directionsUiState.directions.routes.isNotEmpty()) {
                        directionsUiState.directions.routes[0].legs[0].duration.text
                    } else {
                        "No valid routes found, try different addresses"
                    }
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
                    Text(
                        "Approximate Time of Arrival: ${
                            openAiVM.departEpochTime?.plusMillis(
                                60000 * convertDuration(
                                    durationText,
                                    openAiVM
                                )
                            )?.atZone(openAiVM.zoneId)
                                ?.toLocalTime()?.format(dtf)
                        }"
                    )
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
                        floor(weatherUiState.weatherHourly.data[hoursInt!!].temp).toInt()
                            .toString() + "\u2109",
                        fontSize = 48.sp
                    )
                    Text(
                        "Feels like: " + floor(weatherUiState.weatherHourly.data[hoursInt].app_temp).toInt()
                            .toString() + "\u2109"
                    )

                    Image(
                        painterResource(id = weatherIcons[index].iconImage),
                        contentDescription = null,
                        modifier = modifier.size(150.dp),
                    )
                    Text(weatherUiState.weatherHourly.data[hoursInt].weather.description)
                    Text("Precipitation Chance: " + weatherUiState.weatherHourly.data[hoursInt].pop.toString() + "%")
                    Spacer(modifier = modifier.height(16.dp))

                    openAiVM.absoluteLow = weatherUiState.weatherHourly.data[hoursInt].temp
                    openAiVM.absoluteHigh = weatherUiState.weatherHourly.data[hoursInt].temp

                    if (openAiVM.travelTime != 0) {
                        openAiVM.getAnalysis()
                    }
                    if (!openAiVM.responseReceived) {
                        when (val openAiUIState = openAiVM.openAiState) {
                            is OpenAiState.Success -> {
                                openAiVM.chatGPTResponse = openAiUIState.summary
                                openAiVM.responseReceived = true
                                Text(
                                    "Items to Bring: \n" + openAiVM.chatGPTResponse,
                                    fontSize = 20.sp
                                )
                            }

                            is OpenAiState.Loading -> {
                                Text("Loading Travel Analysis", fontSize = 30.sp)
                                LoadingDialog()
                            }

                            is OpenAiState.Error -> {
                                Text("Service Error", fontSize = 30.sp)
                            }
                        }
                    } else {
                        Text("Items to Bring: \n" + openAiVM.chatGPTResponse, fontSize = 20.sp)


                    }

//                    Text("Items to Bring: ")
//                    Text("  \u25CF    Item 1")
//                    Text("  \u25CF    Item 2")
//                    Text("  \u25CF    Item 3")
//                    Text("  \u25CF    Item 4")
                    Spacer(modifier = modifier.height(8.dp))
                    ShowMap(modifier, weatherVM, addressVM)
                    Spacer(modifier = modifier.height(8.dp))

                    Button(onClick = {
                        intentOnClick(
                            weatherVM.weatherLat,
                            weatherVM.weatherLon
                        )
                    }) {
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
                    convertDuration(durationText, openAiVM)
                    Text("Travel Time: $durationText")
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
                    Text("Day of Arrival, " + getBetterDate(weatherUiState.weatherDaily.data[daysDifferenceDeparture!!].valid_date))

                    Text(
                        floor(weatherUiState.weatherDaily.data[daysDifferenceDeparture].max_temp).toInt()
                            .toString() + "\u2109 / "
                                + floor(weatherUiState.weatherDaily.data[daysDifferenceDeparture].min_temp).toInt()
                            .toString() + "\u2109",
                        fontSize = 48.sp
                    )

                    Image(
                        painterResource(id = weatherIcons[index].iconImage),
                        contentDescription = null,
                        modifier = modifier.size(150.dp),
                    )
                    Text(weatherUiState.weatherDaily.data[daysDifferenceDeparture].weather.description)
                    Row {
                        Image(
                            painterResource(id = R.drawable.rainnb),
                            modifier = modifier.size(24.dp),
                            contentDescription = null
                        )
                        Text("     " + weatherUiState.weatherDaily.data[daysDifferenceDeparture].pop.toString() + "%")
                    }
                    Text("Weather Forecast for Duration of Trip", fontSize = 18.sp)

                    LazyRow(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        itemsIndexed(weatherUiState.weatherDaily.data) { index, day ->
                            if (index >= daysDifferenceDeparture && index <= daysDifferenceArrival!!) {
                                WeatherCard(dayData = day, modifier, weatherIcons)
                            }
                        }
                    }

                    Spacer(modifier = modifier.height(16.dp))
                    val highTemps: MutableList<Double> = mutableListOf()

                    weatherUiState.weatherDaily.data.forEachIndexed { index, weatherDetailsDaily ->
                        if (index >= daysDifferenceDeparture && index <= daysDifferenceArrival!!) {
                            highTemps.add(weatherDetailsDaily.max_temp)
                        }
                    }
                    openAiVM.absoluteHigh = highTemps.max()

                    val lowTemps: MutableList<Double> = mutableListOf()

                    weatherUiState.weatherDaily.data.forEachIndexed { index, weatherDetailsDaily ->
                        if (index >= daysDifferenceDeparture && index <= daysDifferenceArrival!!) {
                            lowTemps.add(weatherDetailsDaily.min_temp)
                        }
                    }
                    openAiVM.absoluteLow = highTemps.min()

                    if (openAiVM.travelTime != 0) {
                        openAiVM.getAnalysis()
                    }
                    if (!openAiVM.responseReceived) {
                        when (val openAiUIState = openAiVM.openAiState) {
                            is OpenAiState.Success -> {
                                openAiVM.chatGPTResponse = openAiUIState.summary
                                openAiVM.responseReceived = true
                                Text(
                                    "Items to Bring: \n" + openAiVM.chatGPTResponse,
                                    fontSize = 20.sp
                                )
                            }

                            is OpenAiState.Loading -> {
                                Text("Loading Travel Analysis", fontSize = 30.sp)
                                LoadingDialog()
                            }

                            is OpenAiState.Error -> {
                                Text("Service Error", fontSize = 30.sp)
                            }
                        }
                    } else {
                        Text("Items to Bring: \n" + openAiVM.chatGPTResponse, fontSize = 20.sp)


                    }
//                    Spacer(modifier = Modifier.height(10.dp))
//                    Text("  \u25CF    Item 1")
//                    Text("  \u25CF    Item 2")
//                    Text("  \u25CF    Item 3")
//                    Text("  \u25CF    Item 4")
                    Spacer(modifier = modifier.height(8.dp))
                    ShowMap(modifier, weatherVM, addressVM)
                    Spacer(modifier = modifier.height(8.dp))
                    Button(onClick = {
                        intentOnClick(
                            weatherVM.weatherLat,
                            weatherVM.weatherLon
                        )
                    }) {
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
                    painterResource(id = R.drawable.rainnb),
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
fun convertDuration(duration: String, openAiVM: OpenAiVM): Long {
    if (duration.isNotEmpty()) {
        var numHours: Int = 0
        var numMinutes: Int = 0
        var minutesString: String = duration
        if (duration.contains("hours")) {
            if (duration[1].isDigit()) {
                numHours = duration[0].toString().plus(duration[1].toString()).toInt()
                minutesString = duration.removeRange(0, 9)
            } else {
                numHours = duration[0].toString().toInt()
                minutesString = duration.removeRange(0, 8)
            }
        }
        if (minutesString[1].isDigit()) {
            minutesString = minutesString.removeRange(2, minutesString.length)
            numMinutes = minutesString.toInt()
        } else {
            numMinutes = minutesString[0].toString().toInt()
        }
        numMinutes += (60 * numHours)

        openAiVM.travelTime = numMinutes
        println("Travel Time: ${openAiVM.travelTime}")
        return numMinutes.toLong()

    } else {
        return 0
    }
}

@Composable
fun ShowMap(modifier: Modifier, weatherVM: WeatherViewModel, addressVM: GeocodingViewModel) {
    GoogleMap(
        modifier = modifier
            .fillMaxSize()
            .height(250.dp),

        cameraPositionState = CameraPositionState(
            CameraPosition(
                LatLng(
                    weatherVM.weatherLat,
                    weatherVM.weatherLon
                ), 12f, 1f, 1f
            )
        ),
    ) {
        val parsedPosition = LatLng(weatherVM.weatherLat, weatherVM.weatherLon)
        Marker(
            state = rememberMarkerState(position = parsedPosition),
            draggable = true,
            title = addressVM.DestinationAddressText,
        )
    }
}

// dialog
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoadingDialog(
    cornerRadius: Dp = 16.dp,
    progressIndicatorColor: Color = Color(0xFF35898f),
    progressIndicatorSize: Dp = 80.dp
) {

    val showDialog by mutableStateOf(true)

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 42.dp, end = 42.dp) // margin
                    .background(color = Color.White, shape = RoundedCornerShape(cornerRadius))
                    .padding(top = 36.dp, bottom = 36.dp), // inner padding
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ProgressIndicatorLoading(
                    progressIndicatorSize = progressIndicatorSize,
                    progressIndicatorColor = progressIndicatorColor
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Please wait...",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    }

}

@Composable
private fun ProgressIndicatorLoading(
    progressIndicatorSize: Dp,
    progressIndicatorColor: Color
) {
    val infiniteTransition = rememberInfiniteTransition()

    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 600
            }
        ), label = ""
    )

    CircularProgressIndicator(
        progress = 1f,
        modifier = Modifier
            .size(progressIndicatorSize)
            .rotate(angle)
            .border(
                12.dp,
                brush = Brush.sweepGradient(
                    listOf(
                        Color.White, // add background color first
                        progressIndicatorColor.copy(alpha = 0.1f),
                        progressIndicatorColor
                    )
                ),
                shape = CircleShape
            ),
        strokeWidth = 1.dp,
        color = Color.White // Set background color
    )

}
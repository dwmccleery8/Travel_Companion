package com.example.travelcompanion.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelcompanion.R
import com.example.travelcompanion.viewmodels.OpenAiState
import com.example.travelcompanion.viewmodels.OpenAiVM
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(
    openAiVM: OpenAiVM,
    context: Context,
    onNext: ()-> Unit = {}
) {
    val calendarState = rememberUseCaseState()
    var isOutside by remember { mutableStateOf(false)}
    val tripTypeOptions = arrayOf("Business", "Lesiure")
    var amountOfPeople by remember { mutableIntStateOf(0)}
    var expandedVenue by remember { mutableStateOf(false) }
    var tripTypeSelectedText by remember { mutableStateOf(tripTypeOptions[0]) }
    var returnDate by remember { mutableStateOf(LocalDate.now())}
    var departDate by remember { mutableStateOf(LocalDate.now())}
    var otherUsefulInfo by remember { mutableStateOf("")}

    var startGenerate : Boolean by rememberSaveable {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            Text(text = "Are you traveling for business or leisure?")
            // Venue Type Selection
            ExposedDropdownMenuBox(
                expanded = expandedVenue,
                onExpandedChange = {
                    expandedVenue = !expandedVenue
                }
            ) {
                TextField(
                    value = tripTypeSelectedText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedVenue) },
                    modifier = Modifier.menuAnchor()

                )

                ExposedDropdownMenu(
                    expanded = expandedVenue,
                    onDismissRequest = { expandedVenue = false }
                ) {
                    tripTypeOptions.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                tripTypeSelectedText = item
                                openAiVM.tripType = item
                                expandedVenue = false
                            }
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "Outdoors?")
            Checkbox(checked = isOutside, onCheckedChange = {
                isOutside = !isOutside
                openAiVM.isOutside = isOutside
            } )

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "How many people are going?")
            TextField(value = amountOfPeople.toString(), onValueChange = {
                amountOfPeople = it.toInt() //it is the new string input by the user
                openAiVM.amountOfPeople = amountOfPeople
            },
                modifier = Modifier.fillMaxWidth(),
                label = {Text("Amount of People:")},
                singleLine = true,
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.baseline_person_outline_24),
                        contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "What is the duration of your trip?")
            CalendarDialog(state = calendarState, selection = CalendarSelection.Period {startDate,endDate ->
                departDate = startDate
                openAiVM.departDate = startDate
                returnDate = endDate
                openAiVM.returnDate = returnDate
            })
            Button(onClick = {calendarState.show()}){
                Text(text = "Select Date")
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Departure Date")
            Text(text = departDate.toString())

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Arrival Date")
            Text(text = returnDate.toString())

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "Any other useful information to know?")
            TextField(value = otherUsefulInfo, onValueChange = {
                otherUsefulInfo = it
                openAiVM.otherUsefulInfo = otherUsefulInfo
            },
                modifier = Modifier.fillMaxWidth(),
                label = {Text("Other Information:")},
                singleLine = true,
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.baseline_info_24),
                        contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(10.dp))

            if(startGenerate){
                when(val openAiUIState = openAiVM.openAiState){
                    is OpenAiState.Success -> {
                        Text("Travel Analysis" + openAiUIState.summary, fontSize = 20.sp)
                    }
                    is OpenAiState.Loading -> {
                        Text("Loading Travel Analysis", fontSize = 30.sp)
                    }
                    is OpenAiState.Error -> {
                        Text("Service Error", fontSize = 30.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                startGenerate = !startGenerate
                openAiVM.getAnalysis()
            }, enabled = !startGenerate) {
                Text("Generate", fontSize = 10.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onNext
            ) {
                Text("Go to Map Screen")
            }
        }

    }
}

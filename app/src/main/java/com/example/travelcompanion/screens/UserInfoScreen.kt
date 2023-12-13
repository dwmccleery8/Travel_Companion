package com.example.travelcompanion.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelcompanion.R
import com.example.travelcompanion.models.OpenAiState
import com.example.travelcompanion.models.OpenAiVM
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalDate
import java.time.Period

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(
    openAiVM: OpenAiVM,
    context: Context,
    onNext: ()-> Unit = {}
) {

    val calendarState = rememberUseCaseState()
    val clockState = rememberUseCaseState()
    val clockState2 = rememberUseCaseState()
    val calendarConfig = remember{CalendarConfig(
        boundary = (LocalDate.now()..LocalDate.now().plusDays(16))
    )}
    val tripTypeOptions = arrayOf("Business", "Leisure")
    var expandedVenue by remember { mutableStateOf(false) }
    var returnDateSet by remember { mutableStateOf(false) }
    var departDateSet by remember { mutableStateOf(false) }

//    var startGenerate : Boolean by rememberSaveable {
//        mutableStateOf(false)
//    }

    Image(
        painter = painterResource(id = R.drawable.app_background),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = 0.5f
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        CalendarDialog(state = calendarState, selection = CalendarSelection.Period {startDate,endDate ->
            openAiVM.departDate = startDate
            departDateSet = true
            openAiVM.returnDate = endDate
            returnDateSet = true
        }, config = calendarConfig)

        ClockDialog(state = clockState2, selection = ClockSelection.HoursMinutes {hours, minutes ->
            openAiVM.returnHour = hours
            openAiVM.returnMinute = minutes
        })

        ClockDialog(state = clockState, selection = ClockSelection.HoursMinutes {hours, minutes ->
            openAiVM.departHour = hours
            openAiVM.departMinute = minutes
        })

        LazyColumn() {
            item{
                Text(
                    text = "Tell us a little bit about your trip...",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Are you traveling for business or leisure?",
                    modifier = Modifier.padding(
                        bottom = 5.dp
                    ))

                ExposedDropdownMenuBox(
                    expanded = expandedVenue,
                    onExpandedChange = {
                        expandedVenue = !expandedVenue
                    }
                ) {
                    TextField(
                        value = openAiVM.tripType,
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
                                    openAiVM.tripType = item
                                    expandedVenue = false
                                }
                            )
                        }

                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Outdoors?")
                Checkbox(checked = openAiVM.isOutside, onCheckedChange = {
                    openAiVM.isOutside = it
                } )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "How many people are going?",
                    modifier = Modifier.padding(
                        bottom = 5.dp
                    ))

                TextField(value = openAiVM?.amountOfPeople.toString(), onValueChange = {
                    openAiVM.amountOfPeople = it.toInt()
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
            }


            item {
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "What is the duration of your trip?")
                Button(onClick = {calendarState.show()}){
                    Text(text = "Select Date")
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

                if (openAiVM.departDate!=null && openAiVM.returnDate!=null){
                    if (Period.between(openAiVM?.departDate,openAiVM?.returnDate).days<1){
                        Text(text = "What is your estimated departure time?")


                        Button(onClick = {clockState.show()}){
                            Text(text = "Select Departure Time")
                        }

                        Text(text = "What is your estimated return time?")

                        Button(onClick = {clockState2.show()}){
                            Text(text = "Select Return Time")
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

                Row (modifier = Modifier.height(40.dp)) {
                    Icon(painter = painterResource(id = R.drawable.baseline_arrow_circle_right_24), contentDescription = null)
                    if (departDateSet){
                        Text(text = " ${openAiVM?.departDate}")
                        if ((Period.between(openAiVM?.departDate,openAiVM?.returnDate).days<1)&&(openAiVM.departHour!=null)&&(openAiVM?.departMinute!=null)){
                            var amORpm: String = "AM"
                            var printHour: Int = openAiVM.departHour!!
                            var printMinutes: String = openAiVM.departMinute.toString()
                            if (openAiVM.departHour!! >11){
                                amORpm = "PM"
                                if (openAiVM.departHour!! >12) {
                                    printHour -= 12
                                }
                            }
                            if (openAiVM.departMinute!! <10){
                                printMinutes = "0$printMinutes"
                            }
                            Text(text = " @ $printHour:$printMinutes $amORpm")
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

                Row (modifier = Modifier.height(40.dp)) {
                    Icon(painter = painterResource(id = R.drawable.baseline_arrow_circle_left_24), contentDescription = null)
                    if (returnDateSet) {
                        Text(text = " ${openAiVM.returnDate}")
                        if ((Period.between(openAiVM.departDate,openAiVM.returnDate).days<1)&&(openAiVM.returnHour!=null)&&(openAiVM.returnMinute!=null)){
                            var amORpm: String = "AM"
                            var printHour: Int = openAiVM.returnHour!!
                            var printMinutes: String = openAiVM.returnMinute.toString()
                            if (openAiVM.returnHour!! >11){
                                amORpm = "PM"
                                if (openAiVM.returnHour!! >12) {
                                    printHour -= 12
                                }
                            }
                            if (openAiVM.returnMinute!! <10){
                                printMinutes = "0$printMinutes"
                            }
                            Text(text = " @ $printHour:$printMinutes $amORpm")
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Any other useful information to know?",
                    modifier = Modifier.padding(
                        bottom = 5.dp
                    ))

                var usefulString: String
                if (openAiVM.otherUsefulInfo!=null){
                    usefulString = openAiVM.otherUsefulInfo!!
                }else{
                    usefulString = ""
                }
                TextField(value = usefulString, onValueChange = {
                    openAiVM.otherUsefulInfo = it
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
            }

//            Spacer(modifier = Modifier.height(10.dp))
//
//            if(startGenerate){
//                when(val openAiUIState = openAiVM.openAiState){
//                    is OpenAiState.Success -> {
//                        Text("Travel Analysis" + openAiUIState.summary, fontSize = 20.sp)
//                    }
//                    is OpenAiState.Loading -> {
//                        Text("Loading Travel Analysis", fontSize = 30.sp)
//                    }
//                    is OpenAiState.Error -> {
//                        Text("Service Error", fontSize = 30.sp)
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            Button(onClick = {
//                startGenerate = !startGenerate
//                openAiVM.getAnalysis()
//            }, enabled = !startGenerate) {
//                Text("Generate", fontSize = 10.sp)
//            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

                var alphaValue: Float

                if (openAiVM.readyToGo()){
                    alphaValue = 1.0f
                }
                else{
                    alphaValue = 0.5f
                }


                Button(
                    onClick = onNext,
                    modifier = Modifier.alpha(alphaValue),
                    enabled = openAiVM.readyToGo()
                ) {
                    Text("Go to Map Screen")
                }
            }
        }

    }
}

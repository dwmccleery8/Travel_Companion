package com.example.travelcompanion.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelcompanion.apis.OpenAiApi
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.Period
import java.time.ZoneId

sealed interface OpenAiState {

    data class Success(val summary: String) : OpenAiState
    object Loading: OpenAiState
    object Error : OpenAiState

}

@RequiresApi(Build.VERSION_CODES.O)
class OpenAiVM : ViewModel() {

    var tripType: String by mutableStateOf("Select type:")
    var isOutside: Boolean by mutableStateOf(false)
    var amountOfPeople: Int by mutableIntStateOf(0)
    var otherUsefulInfo: String? by mutableStateOf(null)
    var departEpochTime: Instant? by mutableStateOf(null)
    var arrivalEpochTime: Instant? by mutableStateOf(null)
    var returnEpochTime: Instant? by mutableStateOf(null)
    var absoluteLow: Double? by mutableStateOf(null)
    var absoluteHigh: Double? by mutableStateOf(null)
    var chatGPTResponse: String? by mutableStateOf(null)
    var departTimeSet: Boolean by mutableStateOf(false)
    var returnTimeSet: Boolean by mutableStateOf(false)
    var responseReceived: Boolean by mutableStateOf(false)

    var openAiState: OpenAiState by mutableStateOf(OpenAiState.Loading)
        private set

    val zoneId = ZoneId.of("EST")

    fun reset(){
        tripType = "Select type:"
        isOutside = false
        amountOfPeople = 0
        otherUsefulInfo = null
        departEpochTime = null
        arrivalEpochTime = null
        returnEpochTime = null
        absoluteLow = null
        absoluteHigh = null
        chatGPTResponse = null
        responseReceived = false
        departTimeSet = false
        returnTimeSet = false
        openAiState = OpenAiState.Loading
    }

    fun readyToGo(): Boolean{
        if ((amountOfPeople>0)&&(returnEpochTime!=null)&&(departEpochTime!=null)){
            return if (Period.between(departEpochTime!!.atZone(zoneId).toLocalDate(),returnEpochTime!!.atZone(zoneId).toLocalDate()).days<1){
                (departTimeSet)&&(returnTimeSet)
            } else{
                true
            }
        }
        return false
    }

    fun getAnalysis(){

        viewModelScope.launch {
            val isOutsideText: String = if (isOutside){
                "outside"
            }else{
                "indoors"
            }

            try{
                val prompt = "Hey ChatGPT, right now, you are being queried through an API to respond" +
                        " to user input and assist them in their planning of a road trip. " +
                        "Your task is to provide a brief, compressed list (in bullet point format with ONLY 10 bullet points) " +
                        "of travel essentials and items that are commonly forgotten on a $tripType road trip. " +
                        "The venue being traveled to is mainly $isOutsideText and there will be $amountOfPeople people there. " +
                        "This person will be leaving on ${departEpochTime!!.atZone(zoneId).toLocalDate()} and will be returning home on ${returnEpochTime!!.atZone(zoneId).toLocalDate()}. " +
                        "This road trip will take 5 hours and it will be 32 degrees outside and sunny on arrival " +
                        "at the venue and 42 degrees outside and raining when the user returns. Other useful " +
                        "information about this trip inputted by the user includes:  \"$otherUsefulInfo\"."

                val response = OpenAiApi.getResponse(prompt)

                openAiState = OpenAiState.Success(summary = response)

            }catch (e: Exception){

                println("BitcoinVM: error ${e.printStackTrace()}")

                openAiState = OpenAiState.Error
            }

        }

    }

}
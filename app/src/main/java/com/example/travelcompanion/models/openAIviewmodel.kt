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
import java.time.LocalDate
import java.time.Period

sealed interface OpenAiState {

    data class Success(val summary: String) : OpenAiState
    object Loading: OpenAiState
    object Error : OpenAiState

}

@RequiresApi(Build.VERSION_CODES.O)
class OpenAiVM : ViewModel() {

    var tripType by mutableStateOf("Select type:")
    var isOutside by mutableStateOf(false)
    var amountOfPeople by mutableIntStateOf(0)
    var returnDate: LocalDate by mutableStateOf(LocalDate.now().minusDays(1))
    var departDate: LocalDate by mutableStateOf(LocalDate.now().minusDays(2))
    var otherUsefulInfo by mutableStateOf("")
    var departHour: Int by mutableIntStateOf(-1)
    var departMinute: Int by mutableIntStateOf(-1)
    var returnHour: Int by mutableIntStateOf(-1)
    var returnMinute: Int by mutableIntStateOf(-1)

    var openAiState: OpenAiState by mutableStateOf(OpenAiState.Loading)
        private set

    fun reset(){
        tripType = "Select type:"
        isOutside = false
        amountOfPeople = 0
        returnDate = LocalDate.now().minusDays(1)
        departDate = LocalDate.now().minusDays(2)
        otherUsefulInfo = ""
        openAiState = OpenAiState.Loading
    }

    fun readyToGo(): Boolean{
        if ((tripType!="Select type:")&&(amountOfPeople>0)&&(returnDate != LocalDate.now().minusDays(1))&&(departDate != LocalDate.now().minusDays(2))){
            if (Period.between(departDate,returnDate).days<1){
                return (departHour!=-1)&&(departMinute!=-1)&&(returnHour!=-1)&&(returnMinute!=-1)
            }
            else{
                return true
            }
        }
        return false
    }

    fun getAnalysis(){

        viewModelScope.launch {
            var isOutsideText: String
            if (isOutside){
                isOutsideText = "outside"
            }else{
                isOutsideText = "indoors"
            }

            try{
                val prompt = "Hey ChatGPT, right now, you are being queried through an API to respond" +
                        " to user input and assist them in their planning of a road trip. " +
                        "Your task is to provide a brief, compressed list (in bullet point format with ONLY 10 bullet points) " +
                        "of travel essentials and items that are commonly forgotten on a $tripType road trip. " +
                        "The venue being traveled to is mainly $isOutsideText and there will be $amountOfPeople people there. " +
                        "This person will be leaving on $departDate and will be returning home on $returnDate. " +
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
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

sealed interface OpenAiState {

    data class Success(val summary: String) : OpenAiState
    object Loading: OpenAiState
    object Error : OpenAiState

}

@RequiresApi(Build.VERSION_CODES.O)
class OpenAiVM : ViewModel() {

    var tripType by mutableStateOf("")
    var isOutside by mutableStateOf(false)
    var amountOfPeople by mutableIntStateOf(0)
    var returnDate: LocalDate by mutableStateOf(LocalDate.now())
    var departDate: LocalDate by mutableStateOf(LocalDate.now())
    var otherUsefulInfo by mutableStateOf("")

    var openAiState: OpenAiState by mutableStateOf(OpenAiState.Loading)
        private set

    fun getAnalysis(){

        viewModelScope.launch {
            var isOutsideText: String = ""
            if (isOutside){
                isOutsideText = "outside"
            }else{
                isOutsideText = "indoors"
            }

            try{
                val prompts = "Could you provide a brief list of things that are commonly forgotten and advice on a $tripType roadtrip where the venue being traveled to is $isOutsideText\n" +
                        "and will contain $amountOfPeople people. This person will be leaving on $departDate and will be returning home on $returnDate.\n" +
                        "Other useful information about this trip includes: $otherUsefulInfo"

                val response = OpenAiApi.getResponse(prompts)

                openAiState = OpenAiState.Success(summary = response)

            }catch (e: Exception){

                println("BitcoinVM: error ${e.printStackTrace()}")

                openAiState = OpenAiState.Error
            }

        }

    }

}
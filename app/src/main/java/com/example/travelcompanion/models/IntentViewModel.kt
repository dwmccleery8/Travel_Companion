package com.example.travelcompanion.models

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class IntentViewModel: ViewModel() {

    var uri: String? by mutableStateOf(null)
        private set

    var lat: Double? by mutableStateOf(null)
    var long: Double? by mutableStateOf(null)

    fun updateUri(){
        this.uri = "google.navigation:q=$lat,$long&mode=d"
    }

}
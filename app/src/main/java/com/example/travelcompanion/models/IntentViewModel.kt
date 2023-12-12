package com.example.travelcompanion.models

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class IntentViewModel: ViewModel() {
    var uri: String? by mutableStateOf("google.navigation:q=Grove City College&mode=d")

    fun updateUri(uri: String?){
        this.uri = uri
    }

}
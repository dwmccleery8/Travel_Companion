package com.example.travelcompanion

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.travelcompanion.models.IntentViewModel
import com.example.travelcompanion.ui.theme.TravelCompanionTheme

class MainActivity() : ComponentActivity() {

    private val intentVM by viewModels<IntentViewModel>()

//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        val uri = intent?.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
//
//        intentVM.updateUri(uri)
//
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelCompanionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TravelCompanionApp(LocalContext.current, intentOnClick = {intentOnClick()})
//                    WeatherScreen()
//                    AddressAutoCompleteScreen()

                }
            }
        }
    }

    fun intentOnClick(){
        val gmmIntentUri = Uri.parse(intentVM.uri)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(packageManager)?.let {
            startActivity(mapIntent)
        }

    }
}

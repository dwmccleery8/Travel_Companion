package com.example.travelcompanion.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ResultsScreen(
    modifier: Modifier = Modifier,
    onNext: ()-> Unit = {}
) {
    Column(modifier = modifier
        .fillMaxSize()) {

        Text(text = "weather forecast here")
        Text(text = "suggested items to bring: ...")

        Button(
            onClick = onNext
        ) {
            Text("Go Back to Selection Screen")
        }
    }


}
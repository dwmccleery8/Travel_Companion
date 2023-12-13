package com.example.travelcompanion.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.travelcompanion.R

@Composable
fun TitleScreen(
    modifier: Modifier = Modifier,
    onNext: ()-> Unit = {}
) {
    Image(
        painter = painterResource(id = R.drawable.app_background),
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = 0.5f
    )
    Column(modifier = modifier
        .fillMaxSize()) {

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center) {
            Column(modifier = Modifier.weight(0.3f)){}

            Column(modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text = "Welcome to\nTravel Companion",
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }

            Column(modifier = Modifier.weight(0.3f)){}
        }

        Spacer(modifier = Modifier.weight(0.25f))

        Row(modifier = Modifier.weight(0.25f)) {
            Column(modifier = Modifier.weight(1f)){}

            Column(modifier = Modifier.weight(1f)) {
                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text("Begin!")
                }
            }

            Column(modifier = Modifier.weight(1f)){}
        }

        Spacer(modifier = Modifier.weight(2f))
    }


}

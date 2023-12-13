package com.example.travelcompanion.screens

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelcompanion.R

@OptIn(ExperimentalTextApi::class)
@Composable
fun TitleScreen(
    modifier: Modifier = Modifier,
    onNext: ()-> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "scale"
    )
    Box(modifier = Modifier.fillMaxSize().padding(top = 300.dp)) {
        Text(
            text = "Hello",
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin.Center
                }
                .align(Alignment.Center),
            // Text composable does not take TextMotion as a parameter.
            // Provide it via style argument but make sure that we are copying from current theme
            style = LocalTextStyle.current.copy(textMotion = TextMotion.Animated)
        )
    }


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

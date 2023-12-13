package com.example.travelcompanion.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    onNext: ()-> Unit = {}
) {
    val gcc = LatLng(41.155298, -80.079247)
    val cameraPosition = rememberCameraPositionState() {
        position = CameraPosition.fromLatLngZoom(gcc, 15f)
    }
    Column(modifier = modifier.fillMaxSize()) {

        Button(
            onClick = onNext
        ) {
            Text("Go to Results Screen")
        }

        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPosition
        ) {
            Marker(
                state = rememberMarkerState(position = gcc),
                draggable = true,
                title = "Grove City College",
                snippet = "Marker in GCC",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
            )


        }
    }

}
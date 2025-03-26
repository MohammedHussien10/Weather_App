package com.example.weatherapp.mapscreen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.*

//@Composable
//fun MapScreen() {
//    val cairoLocation = LatLng(30.0444, 31.2357)
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(cairoLocation, 5f)
//    }
//
//    GoogleMap(
//        modifier = Modifier.fillMaxSize(),
//        cameraPositionState = cameraPositionState
//    ) {
//        Marker(
//            state = rememberMarkerState(position = cairoLocation),
//            title = "Selected Location"
//        )
//    }
//}

@Composable
fun SelectableMapScreen() {
    var selectedLocation by remember { mutableStateOf(LatLng(30.0444, 31.2357)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedLocation, 5f)
    }

    val markerState = remember { MarkerState(position = selectedLocation) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                selectedLocation = latLng
                markerState.position = latLng
            }
        ) {
            Marker(state = markerState)
        }

        Button(
            onClick = { Log.d("Map", "Selected Location: $selectedLocation") },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Select Location")
        }
    }
}

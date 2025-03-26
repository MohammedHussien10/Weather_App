package com.example.weatherapp.mapscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
fun SelectableMapScreen(navController: NavController) {
    var selectedLocation by remember { mutableStateOf(LatLng(30.0444, 31.2357)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedLocation, 5f)
    }
    val markerState = rememberMarkerState(position = selectedLocation)

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


        MapTopBar(
            onBackClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2C2C5E))
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Selectable City", color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { Log.d("Map", "Selected Location: $selectedLocation") },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Select Location")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapTopBar(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(Color(0xFF2C2C5E))) {
        TopAppBar(
            title = { Text("Map", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
        )
    }
}

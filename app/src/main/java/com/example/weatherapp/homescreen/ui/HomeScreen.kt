package com.example.weatherapp.homescreen.ui

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherapp.data.remote.convertTimestampToDate
import com.example.weatherapp.homescreen.viewmodel.HomeViewModel
import com.google.android.gms.location.LocationServices

@Composable
fun HomeScreen(viewModel: HomeViewModel, apiKey: String) {
    val tempUnit = "metric"
    val weather by viewModel.weatherData.observeAsState()
    val forecast by viewModel.forecastLiveData.observeAsState()
    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    val context = LocalContext.current

// get permission for Gps
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getCurrentLocation(context) { lat, lon ->
                    location = Pair(lat, lon)
                //call fetchWeatherByLocation when get permission
                    viewModel.fetchWeatherByLocation(lat, lon, apiKey, units = tempUnit)
                }
            } else {
                Toast.makeText(context, "Need permission For Location", Toast.LENGTH_SHORT).show()
            }
        }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation(context) { lat, lon ->
                location = Pair(lat, lon)
                //call fetchWeatherByLocation
                Log.i("crood","lat $lat log $lon" ,)
                viewModel.fetchWeatherByLocation(lat, lon, apiKey, units = tempUnit)
                viewModel.fetchWeatherForecast(lat, lon, apiKey, units = tempUnit)
            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2193b0), Color(0xFF6dd5ed))
                )
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        weather?.let { data ->
            Text(text = "🌍 المدينة: ${data.name}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "🌡️ درجة الحرارة: ${data.main.humidity}°C", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "💨 سرعة الرياح: ${data.wind.speed} m/s", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "☁️ حالة الطقس: ${data.weather[0].description}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "☁️ time: ${convertTimestampToDate(data.dt)}", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        } ?: CircularProgressIndicator()
    }

    LazyColumn {
        forecast?.list?.forEach { forecastItem ->
            item {
                Text(text = "Time: ${forecastItem.dt}")
                Text(text = "Temp: ${forecastItem.main.humidity}°C")
                Text(text = "Condition: ${forecastItem.weather[0].description}")
            }
        }
    }


}

private fun getCurrentLocation(context: Context, onLocationReceived: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            onLocationReceived(location.latitude, location.longitude)
        } else {
            Toast.makeText(context, "Need permission For Location", Toast.LENGTH_SHORT).show()
        }
    }
}

package com.example.weatherapp.settingsscreen.ui

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.weatherapp.homescreen.viewmodel.HomeViewModel
import com.example.weatherapp.settingsscreen.viewmodel.SettingsViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

@Composable
fun SettingsScreen(navController: NavHostController, homeViewModel: HomeViewModel, apiKey: String,settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current
    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    val tempUnits = "metric"
    val useGps = remember { mutableStateOf(false) }
    val language by settingsViewModel.language.collectAsState()
    val tempUnit by settingsViewModel.tempUnit.collectAsState()
    val windUnit by settingsViewModel.windSpeedUnit.collectAsState()
    val locationMethod by settingsViewModel.locationMethod.collectAsState(initial = "Gps")

    // save Updates in datastore
    fun saveLanguage(selected: String) {
        settingsViewModel.saveLanguage(selected)
    }

    fun saveTempUnit(selected: String) {
        settingsViewModel.saveTempUnit(selected)
    }

    fun saveWindSpeedUnit(selected: String) {
        settingsViewModel.saveWindSpeedUnit(selected)
    }

    fun saveLocationMethod(selected: String) {
        settingsViewModel.saveLocationMethod(selected)
    }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getCurrentLocation(context) { lat, lon ->
                    location = Pair(lat, lon)
                    homeViewModel.fetchWeatherByLocation(lat, lon, apiKey, units = tempUnits)
                    homeViewModel.fetchWeatherForecast(lat, lon, apiKey, units = tempUnits)
                }
            } else {
                Toast.makeText(context, "Need permission for Location", Toast.LENGTH_SHORT).show()
            }
        }

    // استخدام LaunchedEffect بشكل صحيح
    LaunchedEffect(useGps.value) {
        if (useGps.value) {
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getCurrentLocation(context) { lat, lon ->
                        location = Pair(lat, lon)
                        homeViewModel.fetchWeatherByLocation(lat, lon, apiKey, units = tempUnit)
                        homeViewModel.fetchWeatherForecast(lat, lon, apiKey, units = tempUnit)
                    }
                }

                ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    Toast.makeText(
                        context,
                        "Location permission is required",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B0C2A))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SettingOption("Language", listOf("Arabic", "English"),language) { selected ->
            saveLanguage(selected)
        }

        SettingOption("Temp Unit", listOf("Celsius °C", "Kelvin K", "Fahrenheit °F"),tempUnit) { selected ->
            saveTempUnit(selected)
        }

        SettingOption("Location", listOf("Gps", "Map"), locationMethod) { selected ->
            saveLocationMethod(selected)
            if (selected == "Map") {
                navController.navigate("SelectableMapScreen")
            } else if (selected == "Gps") {
                // عند اختيار GPS، تحديث الموقع تلقائيًا
                useGps.value = true
            }
        }

        SettingOption("Wind Speed Unit", listOf("meter/sec", "mile/hour"),windUnit) { selected ->
            saveWindSpeedUnit(selected)
        }
    }
}


@Composable
fun SettingOption(title: String, options: List<String>, currentValue: String, onOptionSelected: (String) -> Unit) {
    var selectedOption by remember { mutableStateOf(currentValue) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF2193b0))
            .padding(12.dp)
    ) {
        Text(text = title, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)

        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = {
                        selectedOption = option
                        onOptionSelected(option)
                    },
                    colors = RadioButtonDefaults.colors(selectedColor = Color.Cyan)
                )
                Text(
                    text = option,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

fun getCurrentLocation(context: Context, onLocationReceived: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { location ->
            if (location != null) {
                onLocationReceived(location.latitude, location.longitude)
            } else {
                Toast.makeText(context, "Failed to get location", Toast.LENGTH_SHORT).show()
            }
        }
}

package com.example.weatherapp.homescreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.homescreen.viewmodel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel, apiKey: String) {
    val weather by viewModel.weatherData.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchWeather("Cairo", apiKey)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        weather?.let { data ->
            Text(text = "🌍 المدينة: ${data.name}")
            Text(text = "🌡️ درجة الحرارة: ${data.main.temp}°C")
            Text(text = "💨 سرعة الرياح: ${data.wind.speed} m/s")
            Text(text = "☁️ حالة الطقس: ${data.weather[0].description}")
        } ?: CircularProgressIndicator()
    }
}

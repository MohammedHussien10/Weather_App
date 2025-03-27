package com.example.weatherapp.homescreen.ui

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherapp.R
import com.example.weatherapp.data.remote.ForecastResponse
import com.example.weatherapp.data.remote.Weather
import com.example.weatherapp.data.remote.WeatherResponse
import com.example.weatherapp.data.remote.convertTimestampToDate
import com.example.weatherapp.homescreen.viewmodel.HomeViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun HomeScreen(viewModel: HomeViewModel, apiKey: String) {

    val tempUnit = "metric"
    val weather by viewModel.weatherData.observeAsState()
    val iconUrl = weather?.weather?.firstOrNull()?.getIconUrl()
    val forecast by viewModel.forecastLiveData.observeAsState()
    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val context = LocalContext.current

    // طلب الإذن للموقع
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getCurrentLocation(context) { lat, lon ->
                    location = Pair(lat, lon)
                    viewModel.fetchWeatherByLocation(lat, lon, apiKey, units = tempUnit)
                }
            } else {
                Toast.makeText(context, "Need permission For Location", Toast.LENGTH_SHORT).show()
            }
        }

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation(context) { lat, lon ->
                    location = Pair(lat, lon)
                    viewModel.fetchWeatherByLocation(lat, lon, apiKey, units = tempUnit)
                    viewModel.fetchWeatherForecast(lat, lon, apiKey, units = tempUnit)
                }
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                Toast.makeText(context, "Location permission is required", Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    Details(weather, forecast, iconUrl, isLoading)

}


private fun getCurrentLocation(context: Context, onLocationReceived: (Double, Double) -> Unit) {
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

@Composable
fun Details(
    weather: WeatherResponse?,
    forecast: ForecastResponse?,
    iconUrl: String?,
    isLoading: Boolean
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2193b0), Color(0xFF6dd5ed))
                )
            )
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                 CircularProgressIndicator()
            } else {
                TopCurrentWeatherDetails(weather, iconUrl)
                Spacer(modifier = Modifier.height(16.dp))
                CurrentWeatherDetails(weather)
                //  DailyDetails(forecast)
            }


        }
    }
}

@Composable
fun CurrentWeatherDetails(weather: WeatherResponse?) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val temp = weather?.main?.temp?.toInt()

        if (temp != null) {
            Text(text = if (temp<0) "- $temp" else "$temp", fontSize = 50.sp,fontWeight = FontWeight.Bold)
            }
        }


    }



@Composable
fun TopCurrentWeatherDetails(weather: WeatherResponse?, iconUrl: String?) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.Transparent)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        if (weather != null) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "${weather.weather[0].description}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                val feelsLike = weather.main.feels_Like_Human.let {
                    if (it == 0.0) 9.0 else it.toInt()
                }

                Text(
                    text = "Feels_Like : $feelsLike°C",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) { WeatherIcon(iconUrl) }
        if (weather != null) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = stringResource(R.string.today),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
                Text(
                    text = convertTimestampToDate(weather.dt),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }


    }
}

@Composable
fun DailyDetails(forecast: ForecastResponse?) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 120.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2193b0), Color(0xFF6dd5ed))
                )
            )

    ) {
        forecast?.list?.forEach { forecastItem ->
            item {
                Text(text = "Time: ${convertTimestampToDate(forecastItem.dt)}")
                Text(text = "Temp: ${forecastItem.main.humidity}°C")
                Text(text = "Condition: ${forecastItem.weather[0].description}")

            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeatherIcon(iconUrl: String?) {
    if (!iconUrl.isNullOrEmpty()) {
        GlideImage(
            model = iconUrl,
            contentDescription = "Weather Icon",
            modifier = Modifier.size(64.dp)
        )
    }
}
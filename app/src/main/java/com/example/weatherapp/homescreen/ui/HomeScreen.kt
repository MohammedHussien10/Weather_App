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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
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
import com.example.weatherapp.data.remote.convertTimestampToDateOnly
import com.example.weatherapp.data.remote.convertTimestampToDay
import com.example.weatherapp.data.remote.convertTimestampToTimeOnly
import com.example.weatherapp.data.remote.getNextFiveDaysForecast
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

    // get Permission
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
            .verticalScroll(rememberScrollState())
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
                HourlyDetails(forecast, iconUrl)
                Spacer(modifier = Modifier.height(16.dp))
                DailyDetails(weather)
                NextFiveDays(forecast, iconUrl,weather)
            }


        }
    }
}

@Composable
fun TopCurrentWeatherDetails(weather: WeatherResponse?, iconUrl: String?) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        if (weather != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${weather.weather[0].description}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                val feelsLike = weather.main.feels_Like_Human.let {
                    if (it == 0.0) 9
                    else
                        if (it.toInt() < 0) {
                            it
                        } else {

                        }
                }

                Text(
                    text = "Feels_Like : $feelsLike°C",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) { WeatherIcon(iconUrl) }
        if (weather != null) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.today),
                    fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
                Text(
                    text = convertTimestampToDateOnly(weather.dt),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = convertTimestampToTimeOnly(weather.dt),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }


    }
}

@Composable
fun CurrentWeatherDetails(weather: WeatherResponse?) {
    if (weather != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val temp = weather.main.temp.toInt()


            Text(
                text = if (temp < 0) "- $temp" else "$temp",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = weather.sys.country + "," + weather.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.Transparent)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "\uD83C\uDF07 sunset ${convertTimestampToTimeOnly(weather.sys.sunset)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "\uD83C\uDF05 sunrise ${convertTimestampToTimeOnly(weather.sys.sunrise)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}


@Composable
fun HourlyDetails(forecast: ForecastResponse?, iconUrl: String?) {
    if (forecast != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.Transparent)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Hourly Details", fontSize = 30.sp,
                fontWeight = FontWeight.Bold

            )
        }
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp)


        ) {

            forecast.list.forEach { forecastItem ->
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        Alignment.CenterHorizontally
                    ) {
                        val temp = forecastItem.main.temp.toInt()
                        Text(
                            text = " ${convertTimestampToTimeOnly(forecastItem.dt)}",
                            fontSize = 12.sp
                        )
                        WeatherIcon(iconUrl)
                        Text(text = " ${if (temp < 0) "- $temp" else "$temp"}°C")
                    }
                }
            }
        }
    }
}

@Composable
fun DailyDetails(weather: WeatherResponse?) {
    if (weather != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.Transparent)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Daily Details", fontSize = 30.sp,
                fontWeight = FontWeight.Bold

            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.Transparent)
                .padding(16.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (weather != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) { // column one
                    Text(
                        text = "Pressure",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${weather.main.pressure} hpa",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Humidity",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${weather.main.humidity} %",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) { // column two
                    Text(
                        text = "Wind Speed",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${weather.wind.speed} m/s",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Clouds",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${weather.clouds.all} %",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                }

            }
        }
    }
}
//@Composable
//fun tt(forecast: ForecastResponse?, iconUrl: String?){
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//                .background(Color.Transparent)
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = "Next 5 Days", fontSize = 30.sp,
//                fontWeight = FontWeight.Bold
//
//            )
//        }
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(top = 30.dp)
//                .height(300.dp)
//
//        ) {
//
//            forecast.list.forEach { forecastItem ->
//                item {
//                    Row(
//                        modifier = Modifier.fillMaxSize(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        val temp = forecastItem.main.temp.toInt()
//                        Text(
//                            text = " ${convertTimestampToTimeOnly(forecastItem.dt)}",
//                            fontSize = 12.sp
//                        )
//                        WeatherIcon(iconUrl)
//                        Text(text = " ${if (temp < 0) "- $temp" else "$temp"}°C")
//                    }
//                }
//            }
//        }
//    }
//
//}

@Composable
fun NextFiveDays(forecast: ForecastResponse?, iconUrl: String?,weather: WeatherResponse?) {
    if (forecast != null || weather != null)  {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.Transparent)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Next 5 Days", fontSize = 30.sp,
                fontWeight = FontWeight.Bold

            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.Transparent)
                .padding(16.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            forecast?.let {
                val nextFiveDays = getNextFiveDaysForecast(it)

                Column {
                    nextFiveDays.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val temp = item.main.temp.toInt()
                            val day = convertTimestampToDateOnly(item.dt)
                            val today = weather?.let { it1 -> convertTimestampToDateOnly(it1.dt) }
                            Text(text = if (today == day)  today else day)
                            WeatherIcon(iconUrl)
                            Text(text = if (temp < 0) "- $temp" else "$temp°C")
                        }
                    }
                }

            }
        }
    }
}

//WeatherIcon

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
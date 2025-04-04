package com.example.weatherapp.detailsscreen.ui



import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import androidx.compose.runtime.collectAsState
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
import com.example.weatherapp.data.local.models.WeatherDetails
import com.example.weatherapp.data.remote.ForecastResponse
import com.example.weatherapp.data.remote.Weather
import com.example.weatherapp.data.remote.WeatherResponse
import com.example.weatherapp.data.remote.convertTimestampToDate
import com.example.weatherapp.data.remote.convertTimestampToDateOnly
import com.example.weatherapp.data.remote.convertTimestampToDay
import com.example.weatherapp.data.remote.convertTimestampToTimeOnly
import com.example.weatherapp.data.remote.getNextFiveDaysForecast
import com.example.weatherapp.data.remote.toWeatherDetails
import com.example.weatherapp.homescreen.viewmodel.DetailsViewModel
import com.example.weatherapp.homescreen.viewmodel.HomeViewModel
import com.example.weatherapp.settingsscreen.ui.getCurrentLocation
import com.example.weatherapp.settingsscreen.viewmodel.SettingsViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.skydoves.landscapist.glide.GlideImage
import kotlin.math.log

///



@Composable
fun DetailsScreen(city: String, lat: Double, lon: Double,homeViewModel: HomeViewModel,settingsViewModel:SettingsViewModel,apikey:String,detailsViewModel: DetailsViewModel,cityId:Int) {
    val weatherDetails by detailsViewModel.weatherDetails.collectAsState()
    val context = LocalContext.current
    val isConnected = remember { mutableStateOf(isInternetAvailable(context)) }
    val weather by homeViewModel.weatherData.collectAsState()
    val iconUrl = weather?.weather?.firstOrNull()?.getIconUrl()
    val forecast by homeViewModel.forecastData.collectAsState()
    val tempUnit by settingsViewModel.tempUnit.collectAsState(initial = "metric")
    val isLoading by homeViewModel.isLoading.collectAsState(initial = false)
    Log.i("DetailsScreen", "city $city lat $lat: lon $lon idco $cityId")
    LaunchedEffect(lat, lon) {
        if (isConnected.value) {
                homeViewModel.fetchWeatherByLocation(lat, lon, apikey, tempUnit)
                homeViewModel.fetchWeatherForecast(lat, lon, apikey, tempUnit)
                Log.i("dataGps", "Latitude: $lat, tempUnit: $tempUnit")
    }else{
            detailsViewModel.getWeatherByCityId(city, cityId)
        }

    }


    Details(weather, forecast, iconUrl, isLoading,tempUnit,weatherDetails)

}



@Composable
fun Details(
    weather: WeatherResponse?,
    forecast: ForecastResponse?,
    iconUrl: String?,
    isLoading: Boolean,
    tempUnit:String,
    weatherDetails:WeatherDetails?
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
                TopCurrentWeatherDetails(weather, iconUrl,weatherDetails)
                Spacer(modifier = Modifier.height(16.dp))
                CurrentWeatherDetails(weather,tempUnit)
                HourlyDetails(forecast, iconUrl)
                Spacer(modifier = Modifier.height(16.dp))
                DailyDetails(weather)
                NextFiveDays(forecast, iconUrl, weather)
            }


        }
    }
}

@Composable
fun TopCurrentWeatherDetails(weather: WeatherResponse?, iconUrl: String?,weatherDetails:WeatherDetails?) {

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
                Text(
                    text = "${weatherDetails?.sunrise}",
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
fun CurrentWeatherDetails(weather: WeatherResponse?, tempUnit:String) {
    if (weather != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val temp = weather.main.temp.toInt()

            val unit = when (tempUnit) {
                "metric" -> "°C"
                "standard" -> "°K"
                else -> "°F"
            }

            Text(
                text = if (temp < 0) "- $temp $unit" else "$temp $unit",
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


@Composable
fun NextFiveDays(forecast: ForecastResponse?, iconUrl: String?, weather: WeatherResponse?) {
    if (forecast != null || weather != null) {
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
                            Text(text = if (today == day) today else day)
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


fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
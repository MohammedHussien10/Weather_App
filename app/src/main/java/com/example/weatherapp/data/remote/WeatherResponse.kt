package com.example.weatherapp.data.remote

import com.example.weatherapp.data.local.models.WeatherDetails

data class WeatherResponse(
    val id: Int,
    val name: String,
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val sys: Sys,
    val dt: Long,
    val clouds: Clouds
)

data class Coord(
    val lon: Double,
    val lat: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
){
    fun getIconUrl(): String {
        return "https://openweathermap.org/img/wn/${icon}@2x.png"
    }
}

data class Main(
    val temp: Double,
    val feels_Like_Human: Double,
    val temp_Min: Double,
    val temp_Max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Double,
    val deg: Int
)
data class Clouds(
    val all:Int
)

data class Sys(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

fun WeatherResponse.toWeatherDetails(): WeatherDetails {
    return WeatherDetails(
        id = this.id,
        lon = this.coord.lon,
        lat = this.coord.lat,
        name = this.name,
        temp = this.main.temp,
        feels_Like_Human = this.main.feels_Like_Human,
        temp_Min = this.main.temp_Min,
        temp_Max = this.main.temp_Max,
        pressure = this.main.pressure,
        humidity = this.main.humidity,
        windSpeed = this.wind.speed,
        windDeg = this.wind.deg,
        country = this.sys.country,
        sunrise = this.sys.sunrise,
        sunset = this.sys.sunset,
        dt = this.dt,
        isFav = false
    )
}

package com.example.weatherapp.data.remote

data class WeatherResponse(
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

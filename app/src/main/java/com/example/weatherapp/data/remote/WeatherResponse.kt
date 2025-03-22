package com.example.weatherapp.data.remote

data class WeatherResponse(
    val name: String,
    val main: Main,
    val wind: Wind,
    val weather: List<Weather>
)

data class Main(
    val temp: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Double
)

data class Weather(
    val description: String
)

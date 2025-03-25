package com.example.weatherapp.data.remote


    data class ForecastResponse(
        val city: City,
        val list: List<ForecastItem>
    )

    data class City(
        val name: String,
        val country: String
    )

    data class ForecastItem(
        val dt: Long,
        val main: Main,
        val weather: List<Weather>,
        val wind: Wind
    )


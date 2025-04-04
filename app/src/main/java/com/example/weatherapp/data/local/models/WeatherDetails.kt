package com.example.weatherapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "current_weather")
data class WeatherDetails(

    @PrimaryKey
    val id: Int = 0,
    var isFav : Boolean,
    val lon: Double = 0.0,
    val lat: Double = 0.0,
    val name: String = "",
    val temp: Double = 0.0,
    val feels_Like_Human: Double = 0.0,
    val temp_Min: Double = 0.0,
    val temp_Max: Double = 0.0,
    val pressure: Int = 0,
    val humidity: Int = 0,
    val windSpeed: Double = 0.0,
    val windDeg: Int = 0,
    val country: String = "Unknown",
    val sunrise: Long = 0L,
    val sunset: Long = 0L,
    val dt: Long = System.currentTimeMillis() / 1000
)


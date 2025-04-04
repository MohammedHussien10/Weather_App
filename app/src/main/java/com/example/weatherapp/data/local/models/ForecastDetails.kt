package com.example.weatherapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast")
data class ForecastDetails(
    @PrimaryKey (autoGenerate = true)
    val id : Int,
    val cityId : Int,
    val dt: Long,
    val cityName: String,
    val country: String,
    val temp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: Double,
    val windDeg: Int
)

package com.example.weatherapp.data.remote

import retrofit2.http.Query

interface RemoteDataSource {
    suspend fun getWeatherByLocation(latitude: Double, longitude: Double, apiKey: String,units: String): WeatherResponse
    suspend fun getWeatherByCityName(city: String,apiKey: String):WeatherResponse
    suspend fun getWeatherForecast(lat: Double, lon: Double, apiKey: String,units: String): ForecastResponse
}
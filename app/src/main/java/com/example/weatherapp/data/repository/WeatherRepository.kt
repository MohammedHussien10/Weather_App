package com.example.weatherapp.data.repository

import com.example.weatherapp.data.remote.WeatherResponse
import com.example.weatherapp.data.remote.WeatherApiService

class WeatherRepository(private val apiService: WeatherApiService) {
    suspend fun getWeather(city: String, apiKey: String): WeatherResponse {
        return apiService.getWeather(city, apiKey)
    }
}

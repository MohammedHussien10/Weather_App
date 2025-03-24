package com.example.weatherapp.data.repository

import com.example.weatherapp.data.remote.WeatherResponse
import com.example.weatherapp.data.remote.WeatherApiService

class WeatherRepository(private val apiService: WeatherApiService) {
    // دالة جلب الطقس باستخدام اسم المدينة
    suspend fun getWeather(city: String, apiKey: String): WeatherResponse {
        return apiService.getWeather(city, apiKey)
    }

    // دالة جلب الطقس باستخدام الإحداثيات الجغرافية (الخط العرض والطول)
    suspend fun getWeatherByLocation(latitude: Double, longitude: Double, apiKey: String): WeatherResponse {
        return apiService.getWeatherByLocation(latitude, longitude, apiKey)
    }
}

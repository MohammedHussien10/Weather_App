package com.example.weatherapp.data.repository

import com.example.weatherapp.data.remote.ForecastResponse
import com.example.weatherapp.data.remote.RemoteDataSourceImpl
import com.example.weatherapp.data.remote.WeatherResponse
import com.example.weatherapp.data.remote.WeatherApiService

class WeatherRepository(private val remoteDataSourceImpl: RemoteDataSourceImpl) {

    suspend fun getWeatherByCityName(city: String, apiKey: String): WeatherResponse {
        return remoteDataSourceImpl.getWeatherByCityName(city, apiKey)
    }

    suspend fun getWeatherByLocation(latitude: Double, longitude: Double, apiKey: String,units: String): WeatherResponse {
        return remoteDataSourceImpl.getWeatherByLocation(latitude, longitude, apiKey,units)
    }

    suspend fun getWeatherForecast(lat: Double, lon: Double, apiKey: String,units: String): ForecastResponse {
        return remoteDataSourceImpl.getWeatherForecast(lat, lon, apiKey,units)
    }

}

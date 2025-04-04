package com.example.weatherapp.data.local

import com.example.weatherapp.data.local.models.ForecastDetails
import com.example.weatherapp.data.local.models.WeatherDetails
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun insertWeather(weatherResponse: WeatherDetails)
    fun getWeatherByCity(cityName: String,cityId:Int): Flow<WeatherDetails>
    suspend fun deleteWeatherByCity(cityName: String,latitude: Double, longitude: Double)
    fun getAllFavoriteLocations(): Flow<List<WeatherDetails>>

    suspend fun insertForecast(forecastResponse: ForecastDetails)
    fun getForecastByCity(cityName: String): Flow<ForecastDetails>
    suspend fun deleteForecastByCity(cityName: String)
    fun getAllForecasts(): Flow<List<ForecastDetails>>
}
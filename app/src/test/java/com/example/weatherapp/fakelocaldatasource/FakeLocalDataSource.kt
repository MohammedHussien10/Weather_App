package com.example.weatherapp.fakelocaldatasource

import com.example.weatherapp.data.local.LocalDataSource
import com.example.weatherapp.data.local.models.ForecastDetails
import com.example.weatherapp.data.local.models.WeatherDetails
import kotlinx.coroutines.flow.Flow

class FakeLocalDataSource:LocalDataSource {
    override suspend fun insertWeather(weatherResponse: WeatherDetails) {
        TODO("Not yet implemented")
    }

    override fun getWeatherByCity(cityName: String, cityId: Int): Flow<WeatherDetails> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWeatherByCity(
        cityName: String,
        latitude: Double,
        longitude: Double
    ) {
        TODO("Not yet implemented")
    }

    override fun getAllFavoriteLocations(): Flow<List<WeatherDetails>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertForecast(forecastResponse: ForecastDetails) {
        TODO("Not yet implemented")
    }

    override fun getForecastByCity(cityName: String): Flow<ForecastDetails> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteForecastByCity(cityName: String) {
        TODO("Not yet implemented")
    }

    override fun getAllForecasts(): Flow<List<ForecastDetails>> {
        TODO("Not yet implemented")
    }
}
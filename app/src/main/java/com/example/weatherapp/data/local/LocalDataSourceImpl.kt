package com.example.weatherapp.data.local

import com.example.weatherapp.data.local.models.ForecastDetails
import com.example.weatherapp.data.local.models.WeatherDetails
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(    private val weatherDao: WeatherDao):LocalDataSource {

    companion object {
        @Volatile
        private var INSTANCE: LocalDataSourceImpl? = null

        fun getInstance(weatherDao: WeatherDao): LocalDataSourceImpl {
            return INSTANCE ?: synchronized(this) {
                val instance = LocalDataSourceImpl(weatherDao)
                INSTANCE = instance
                instance
            }
        }
    }

    override suspend fun insertWeather(weatherResponse: WeatherDetails) {
        weatherDao.insertWeather(weatherResponse)
    }

    override fun getWeatherByCity(cityName: String,cityId:Int): Flow<WeatherDetails> {
        return weatherDao.getWeatherByCity(cityName,cityId)
    }

    override suspend fun deleteWeatherByCity(cityName: String,latitude: Double, longitude: Double) {
        weatherDao.deleteWeatherByCity(cityName,latitude,longitude)
    }

    override fun getAllFavoriteLocations(): Flow<List<WeatherDetails>> {
        return weatherDao.getAllFavoriteLocations()
    }

    override suspend fun insertForecast(forecastResponse: ForecastDetails) {
        weatherDao.insertForecast(forecastResponse)
    }

    override fun getForecastByCity(cityName: String): Flow<ForecastDetails> {
        return weatherDao.getForecastByCity(cityName)
    }

    override suspend fun deleteForecastByCity(cityName: String) {
        weatherDao.deleteForecastByCity(cityName)
    }

    override fun getAllForecasts(): Flow<List<ForecastDetails>> {
        return weatherDao.getAllForecasts()
    }
}
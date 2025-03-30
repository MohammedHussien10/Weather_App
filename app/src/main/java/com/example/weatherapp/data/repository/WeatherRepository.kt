package com.example.weatherapp.data.repository

import android.content.Context
import com.example.weatherapp.data.datastore.DataStoreManager
import com.example.weatherapp.data.remote.ForecastResponse
import com.example.weatherapp.data.remote.RemoteDataSourceImpl
import com.example.weatherapp.data.remote.WeatherResponse
import com.example.weatherapp.data.remote.WeatherApiService
import kotlinx.coroutines.flow.Flow

class WeatherRepository(private val remoteDataSourceImpl: RemoteDataSourceImpl,private val dataStoreManager: DataStoreManager) {

    val latitude: Flow<Double> = dataStoreManager.latitude
    val longitude: Flow<Double> = dataStoreManager.longitude
    val language: Flow<String> = dataStoreManager.language
    val tempUnit: Flow<String> = dataStoreManager.tempUnit
    val windSpeedUnit: Flow<String> = dataStoreManager.windSpeedUnit
    val locationMethod: Flow<String> = dataStoreManager.locationMethod
    companion object {
        @Volatile
        private var INSTANCE: WeatherRepository? = null

        fun getInstance(context: Context ,apiService: WeatherApiService): WeatherRepository {
            return INSTANCE ?: synchronized(this) {
                val dataStoreManager = DataStoreManager.getInstance(context)
                val remoteDataSourceImpl = RemoteDataSourceImpl.getInstance(apiService)
                val instance = WeatherRepository(remoteDataSourceImpl,dataStoreManager)
                INSTANCE = instance
                instance
            }
        }
    }






    suspend fun getWeatherByCityName(city: String, apiKey: String): WeatherResponse {
        return remoteDataSourceImpl.getWeatherByCityName(city, apiKey)
    }

    suspend fun getWeatherByLocation(latitude: Double, longitude: Double, apiKey: String,units: String): WeatherResponse {
        return remoteDataSourceImpl.getWeatherByLocation(latitude, longitude, apiKey,units)
    }

    suspend fun getWeatherForecast(lat: Double, lon: Double, apiKey: String,units: String): ForecastResponse {
        return remoteDataSourceImpl.getWeatherForecast(lat, lon, apiKey,units)
    }

    suspend fun saveLanguage(language: String) {
        dataStoreManager.saveLanguage(language)
    }

    suspend fun saveTempUnit(unit: String) {
        dataStoreManager.saveTempUnit(unit)
    }

    suspend fun saveWindSpeedUnit(windSpeedUnit: String) {
        dataStoreManager.saveWindSpeedUnit(windSpeedUnit)
    }
    // Save Location (Latitude & Longitude)
    suspend fun saveLocation(latitude: Double, longitude: Double) {
        dataStoreManager.saveLocation(latitude, longitude)


    }

    suspend fun saveLocationMethod(method: String) {
        dataStoreManager.saveLocationMethod(method)
    }

    // Retrieve Latitude and Longitude separately if needed
    fun getLatitudeRepo(): Flow<Double> = dataStoreManager.latitude
    fun getLongitudeRepo(): Flow<Double> = dataStoreManager.longitude

    fun getLocationMethodRepo(): Flow<String> = dataStoreManager.locationMethod
}

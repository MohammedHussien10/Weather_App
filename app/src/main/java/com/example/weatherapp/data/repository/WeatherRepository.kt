package com.example.weatherapp.data.repository

import android.content.Context
import androidx.work.WorkManager
import com.example.weatherapp.data.datastore.DataStoreManager
import com.example.weatherapp.data.enumclasses.AlertType
import com.example.weatherapp.data.local.LocalDataSourceImpl
import com.example.weatherapp.data.local.WeatherDao
import com.example.weatherapp.data.local.models.ForecastDetails
import com.example.weatherapp.data.local.models.WeatherDetails
import com.example.weatherapp.data.remote.ForecastResponse
import com.example.weatherapp.data.remote.RemoteDataSourceImpl
import com.example.weatherapp.data.remote.WeatherResponse
import com.example.weatherapp.data.remote.WeatherApiService
import kotlinx.coroutines.flow.Flow

class WeatherRepository(private val remoteDataSourceImpl: RemoteDataSourceImpl, private val localDataSourceImpl: LocalDataSourceImpl,private val dataStoreManager: DataStoreManager) {

    val latitude: Flow<Double> = dataStoreManager.latitude
    val longitude: Flow<Double> = dataStoreManager.longitude
    val language: Flow<String> = dataStoreManager.language
    val tempUnit: Flow<String> = dataStoreManager.tempUnit
    val windSpeedUnit: Flow<String> = dataStoreManager.windSpeedUnit
    val locationMethod: Flow<String> = dataStoreManager.locationMethod

    companion object {
        @Volatile
        private var INSTANCE: WeatherRepository? = null

        fun getInstance(context: Context, apiService: WeatherApiService, weatherDao: WeatherDao): WeatherRepository {
            return INSTANCE ?: synchronized(this) {
                val dataStoreManager = DataStoreManager.getInstance(context)
                val workManager = WorkManager.getInstance(context)
                val remoteDataSourceImpl = RemoteDataSourceImpl.getInstance(apiService)
                val localDataSourceImpl = LocalDataSourceImpl.getInstance(weatherDao)
                val instance = WeatherRepository(remoteDataSourceImpl,localDataSourceImpl,dataStoreManager)
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

////////////////////////////////favourites



    suspend fun insertWeather(weatherDetails: WeatherDetails) {
        localDataSourceImpl.insertWeather(weatherDetails)
    }


    fun getWeatherByCity(cityName: String,id:Int): Flow<WeatherDetails> {
        return localDataSourceImpl.getWeatherByCity(cityName,id)
    }


    suspend fun deleteWeatherByCity(cityName: String,latitude: Double, longitude: Double) {
        localDataSourceImpl.deleteWeatherByCity(cityName,latitude,longitude)
    }


    fun getAllFavoriteLocations(): Flow<List<WeatherDetails>> {
        return localDataSourceImpl.getAllFavoriteLocations()
    }


    suspend fun insertForecast(forecastResponse: ForecastDetails) {
        localDataSourceImpl.insertForecast(forecastResponse)
    }


    fun getForecastByCity(cityName: String): Flow<ForecastDetails> {
        return localDataSourceImpl.getForecastByCity(cityName)
    }


    suspend fun deleteForecastByCity(cityName: String) {
        localDataSourceImpl.deleteForecastByCity(cityName)
    }

    fun getAllForecasts(): Flow<List<ForecastDetails>> {
        return localDataSourceImpl.getAllForecasts()
    }





    //////alarms


     fun createWeatherAlertWork(context: Context, hour: Int, minute: Int, alertType: AlertType) {
        // استدعاء الـ WorkManager لإنشاء العمل بناءً على التنبيه الذي تم تحديده
//        val workRequest = OneTimeWorkRequestBuilder<WeatherAlertWorker>()
//            .setInitialDelay(duration, TimeUnit.HOURS) // تحديد المدة بناءً على الساعات
//            .setInputData(workDataOf("alertType" to alertType.name)) // إرسال نوع التنبيه للعمل
//            .build()
        createWeatherAlertWork(context,hour,minute,alertType)
    }

     fun cancelWeatherAlert(context: Context) {
        // إلغاء كل الأعمال الخاصة بالتنبيه
        WorkManager.getInstance(context).cancelAllWork()
    }

}

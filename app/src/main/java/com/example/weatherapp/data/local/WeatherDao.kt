package com.example.weatherapp.data.local
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.local.models.ForecastDetails
import com.example.weatherapp.data.local.models.WeatherDetails

import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherResponse: WeatherDetails)

    @Query("SELECT * FROM current_weather WHERE name = :cityName AND id = :cityId")
    fun getWeatherByCity(cityName: String,cityId:Int): Flow<WeatherDetails>

    @Query("DELETE FROM current_weather WHERE name = :cityName AND lat = :latitude AND lon = :longitude")
    suspend fun deleteWeatherByCity(cityName: String,latitude: Double, longitude: Double)

    @Query("SELECT * FROM current_weather WHERE isFav = 1")
    fun getAllFavoriteLocations(): Flow<List<WeatherDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecastResponse: ForecastDetails)

    @Query("SELECT * FROM forecast WHERE cityName = :cityName")
    fun getForecastByCity(cityName: String): Flow<ForecastDetails>

    @Query("DELETE FROM forecast WHERE cityName = :cityName")
    suspend fun deleteForecastByCity(cityName: String)

    @Query("SELECT * FROM forecast")
    fun getAllForecasts(): Flow<List<ForecastDetails>>
}
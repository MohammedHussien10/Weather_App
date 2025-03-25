package com.example.weatherapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    //get weather by city name
    @GET("data/2.5/weather")
    suspend fun getWeatherByCityName(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): WeatherResponse

    @GET("data/2.5/weather")
    //get weather by latitude and longitude and units
    suspend fun getWeatherByLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse

        @GET("data/2.5/forecast")
        suspend fun getWeatherForecast(
            @Query("lat") latitude: Double,
            @Query("lon") longitude: Double,
            @Query("appid") apiKey: String,
            @Query("units") units: String = "metric"
        ): ForecastResponse



}

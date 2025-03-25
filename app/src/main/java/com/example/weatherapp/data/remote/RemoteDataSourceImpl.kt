package com.example.weatherapp.data.remote

class RemoteDataSourceImpl(private val apiService: WeatherApiService) : RemoteDataSource {
    override suspend fun getWeatherByLocation(latitude: Double, longitude: Double, apiKey: String,units: String): WeatherResponse {
        return apiService.getWeatherByLocation(latitude, longitude, apiKey)
    }

    override suspend fun getWeatherByCityName(city: String, apiKey: String):WeatherResponse {
        return apiService.getWeatherByCityName(city, apiKey)
    }

    override suspend fun getWeatherForecast(lat: Double, lon: Double, apiKey: String,units:String): ForecastResponse {
        return apiService.getWeatherForecast(lat, lon, apiKey,units)
    }


}
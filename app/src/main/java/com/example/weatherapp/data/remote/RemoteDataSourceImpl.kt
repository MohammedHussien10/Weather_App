package com.example.weatherapp.data.remote

class RemoteDataSourceImpl(private val apiService: WeatherApiService) : RemoteDataSource {

    companion object {
        @Volatile
        private var INSTANCE: RemoteDataSourceImpl? = null

        fun getInstance(apiService: WeatherApiService): RemoteDataSourceImpl {
            return INSTANCE ?: synchronized(this) {
                val instance = RemoteDataSourceImpl(apiService)
                INSTANCE = instance
                instance
            }
        }
    }

    override suspend fun getWeatherByLocation(latitude: Double, longitude: Double, apiKey: String,units: String): WeatherResponse {
        return apiService.getWeatherByLocation(latitude, longitude, apiKey,units)
    }

    override suspend fun getWeatherByCityName(city: String, apiKey: String):WeatherResponse {
        return apiService.getWeatherByCityName(city, apiKey)
    }

    override suspend fun getWeatherForecast(lat: Double, lon: Double, apiKey: String,units:String): ForecastResponse {
        return apiService.getWeatherForecast(lat, lon, apiKey,units)
    }


}
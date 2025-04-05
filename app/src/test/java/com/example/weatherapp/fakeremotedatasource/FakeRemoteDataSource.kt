package com.example.weatherapp.fakeremotedatasource

import com.example.weatherapp.data.remote.Clouds
import com.example.weatherapp.data.remote.Coord
import com.example.weatherapp.data.remote.ForecastResponse
import com.example.weatherapp.data.remote.Main
import com.example.weatherapp.data.remote.RemoteDataSource
import com.example.weatherapp.data.remote.RemoteDataSourceImpl
import com.example.weatherapp.data.remote.Sys
import com.example.weatherapp.data.remote.Weather
import com.example.weatherapp.data.remote.WeatherResponse
import com.example.weatherapp.data.remote.Wind
import org.mockito.Mockito.mock

class FakeRemoteDataSource : RemoteDataSource {
    val fakeWeatherResponse = WeatherResponse(
        name = "Cairo",
        weather = listOf(
            Weather(
                id = 1,
                main = "Clear",
                description = "Clear sky",
                icon = "01d"
            )
        ),
        main = Main(
            temp = 25.0,
            pressure = 1013,
            humidity = 80,
            feels_Like_Human = 26.0,
            temp_Min = 20.0,
            temp_Max = 30.0
        ),
        wind = Wind(
            speed = 5.0, deg = 1
        ),
        id = 1,
        coord = Coord(
            lon = 31.2357,
            lat = 30.0444
        ),
        dt = 1618317040L,
        sys = Sys(
            country = "EG",
            sunrise = 1618282134L,
            sunset = 1618333901L
        ),
        clouds = Clouds(
            all = 0
        )
    )

    override suspend fun getWeatherByCityName(city: String, apiKey: String): WeatherResponse {
        return fakeWeatherResponse
    }

    override suspend fun getWeatherForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String
    ): ForecastResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherByLocation(latitude: Double, longitude: Double, apiKey: String, units: String): WeatherResponse {
        return fakeWeatherResponse
    }
}
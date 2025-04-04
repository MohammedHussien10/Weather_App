package com.example.weatherapp.homescreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.remote.ForecastResponse
import com.example.weatherapp.data.remote.WeatherResponse
import com.example.weatherapp.data.remote.toWeatherDetails
import com.example.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData

    private val _forecastData = MutableStateFlow<ForecastResponse?>(null)
    val forecastData: StateFlow<ForecastResponse?> = _forecastData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun getWeatherByCityName (city: String, apiKey: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getWeatherByCityName(city, apiKey)
                _weatherData.value = response
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching weather: ${e.message}"
                Log.e("HomeViewModel", "Error fetching weather", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchWeatherByLocation(latitude: Double, longitude: Double, apiKey: String,units: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getWeatherByLocation(latitude, longitude, apiKey,units)
                _weatherData.value = response
                val weatherDetails = response.toWeatherDetails()
                weatherDetails.isFav = false
                repository.insertWeather(weatherDetails)
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather by location", e)
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchWeatherForecast(lat: Double, lon: Double, apiKey: String,units: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getWeatherForecast(lat, lon, apiKey,units)
                _forecastData.value = response
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching forecast: ${e.message}")
            }finally {
                _isLoading.value = false
            }
        }
    }
}


////database

class HomeViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


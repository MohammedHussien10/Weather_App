package com.example.weatherapp.homescreen.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.remote.ForecastResponse
import com.example.weatherapp.data.remote.WeatherResponse
import com.example.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _weatherData = MutableLiveData<WeatherResponse?>()
    val weatherData: LiveData<WeatherResponse?> = _weatherData
    val isLoading = MutableLiveData<Boolean>()

    private val _forecastLiveData = MutableLiveData<ForecastResponse>()
    val forecastLiveData: LiveData<ForecastResponse> get() = _forecastLiveData

    fun getWeatherByCityName (city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getWeatherByCityName(city, apiKey)
                _weatherData.postValue(response)
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather", e)
            }
        }
    }

    fun fetchWeatherByLocation(latitude: Double, longitude: Double, apiKey: String,units: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = repository.getWeatherByLocation(latitude, longitude, apiKey,units)
                _weatherData.postValue(response)
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather by location", e)
            }finally {
                isLoading.value = false
            }
        }
    }

    fun fetchWeatherForecast(lat: Double, lon: Double, apiKey: String,units: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = repository.getWeatherForecast(lat, lon, apiKey,units)
                _forecastLiveData.postValue(response)
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching forecast: ${e.message}")
            }finally {
                isLoading.value = false
            }
        }
    }
}

class ViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


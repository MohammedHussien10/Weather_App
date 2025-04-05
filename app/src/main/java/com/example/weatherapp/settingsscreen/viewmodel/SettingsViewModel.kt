package com.example.weatherapp.settingsscreen.viewmodel

import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.homescreen.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel( private val repository: WeatherRepository) : ViewModel(){
    val latitude: StateFlow<Double> = repository.latitude.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        30.033333
    )

    val longitude: StateFlow<Double> = repository.longitude.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        31.233334
    )


    fun saveLocation(lat: Double, long: Double) {
        viewModelScope.launch {
            repository.saveLocation(lat, long)
        }
    }

    val language: StateFlow<String> = repository.language.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "English"
    )

    val tempUnit: StateFlow<String> = repository.tempUnit.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "metric"
    )

    val windSpeedUnit: StateFlow<String> = repository.windSpeedUnit.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "m/s"
    )

    val locationMethod: StateFlow<String> = repository.locationMethod.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "Gps"
    )



    fun saveLanguage(language: String) {
        viewModelScope.launch {
            repository.saveLanguage(language)
        }
    }

    fun saveTempUnit(unit: String) {
        viewModelScope.launch {
            repository.saveTempUnit(unit)
        }
    }
    fun saveWindSpeedUnit(windSpeedUnit: String) {
        viewModelScope.launch {
            repository.saveWindSpeedUnit(windSpeedUnit)
        }
    }

    fun saveLocationMethod(method: String) {
        viewModelScope.launch {
            repository.saveLocationMethod(method)
        }
    }



    class SettingsModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                return SettingsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


}
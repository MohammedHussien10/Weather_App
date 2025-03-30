package com.example.weatherapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

// DataStore Instance


class DataStoreManager(private val context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: DataStoreManager? = null

        fun getInstance(context: Context): DataStoreManager {
            return INSTANCE ?: synchronized(this) {
                val instance = DataStoreManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    private val Context.dataStore by preferencesDataStore(name = "user_settings")

    private val LANGUAGE_KEY = stringPreferencesKey("language")
    private val TEMP_UNIT_KEY = stringPreferencesKey("temp_unit")
    private val WIND_UNIT_KEY = stringPreferencesKey("wind_speed_unit")
    private val GPS_ENABLED_KEY = booleanPreferencesKey("gps_enabled")
    private val LATITUDE_KEY = doublePreferencesKey("latitude")
    private val LONGITUDE_KEY = doublePreferencesKey("longitude")
    private val LOCATION_METHOD_KEY = stringPreferencesKey("location_method")

    // Language
    val language: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[LANGUAGE_KEY] ?: "English" }

    suspend fun saveLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    // Temperature Unit
    val tempUnit: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[TEMP_UNIT_KEY] ?: "metric" }

    suspend fun saveTempUnit(unit: String) {
        context.dataStore.edit { preferences ->
            preferences[TEMP_UNIT_KEY] = unit
        }
    }

    // Wind Speed Unit
    val windSpeedUnit: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[WIND_UNIT_KEY] ?: "m/s" }

    suspend fun saveWindSpeedUnit(windSpeedUnit: String) {
        context.dataStore.edit { preferences ->
            preferences[WIND_UNIT_KEY] = windSpeedUnit
        }
    }

    // GPS Enabled
    val gpsEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[GPS_ENABLED_KEY] ?: false }

    suspend fun saveGpsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[GPS_ENABLED_KEY] = enabled
        }
    }

    // Latitude
    val latitude: Flow<Double> = context.dataStore.data
        .map { preferences -> preferences[LATITUDE_KEY] ?: 0.0 }

    suspend fun saveLatitude(lat: Double) {
        context.dataStore.edit { preferences ->
            preferences[LATITUDE_KEY] = lat
        }
    }

    // Longitude
    val longitude: Flow<Double> = context.dataStore.data
        .map { preferences -> preferences[LONGITUDE_KEY] ?: 0.0 }

    suspend fun saveLongitude(lon: Double) {
        context.dataStore.edit { preferences ->
            preferences[LONGITUDE_KEY] = lon
        }
    }

    // Save Location (Latitude & Longitude) together
    suspend fun saveLocation(lat: Double, lon: Double) {
        context.dataStore.edit { preferences ->
            preferences[LATITUDE_KEY] = lat
            preferences[LONGITUDE_KEY] = lon
        }
    }

    // Location Method
    val locationMethod: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[LOCATION_METHOD_KEY] ?: "Gps" }

    suspend fun saveLocationMethod(method: String) {
        context.dataStore.edit { preferences ->
            preferences[LOCATION_METHOD_KEY] = method
        }
    }
}

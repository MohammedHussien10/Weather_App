package com.example.weatherapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

        val LANGUAGE_KEY = stringPreferencesKey("language")
        val TEMP_UNIT_KEY = stringPreferencesKey("temp_unit")
        val WIND_UNIT_KEY = stringPreferencesKey("wind_speed_unit")
        val GPS_ENABLED_KEY = booleanPreferencesKey("gps_enabled")


    // Save Language
    suspend fun saveLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    // Read Language
    val language: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[LANGUAGE_KEY] ?: "English"
        }

    // Save Temperature Unit
    suspend fun saveTempUnit(unit: String) {
        context.dataStore.edit { preferences ->
            preferences[TEMP_UNIT_KEY] = unit
        }
    }

    // Read Temperature Unit
    val tempUnit: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[TEMP_UNIT_KEY] ?: "metric"
        }

    // Save windSpeedUnit
    suspend fun saveWindSpeedUnit(windSpeedUnit: String) {
        context.dataStore.edit { preferences ->
            preferences[WIND_UNIT_KEY] = windSpeedUnit
        }
    }

    // Read windSpeedUnit
    val windSpeedUnit: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[WIND_UNIT_KEY] ?: "meter/sec"
        }
}
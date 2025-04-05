package com.example.weatherapp.fakedatastoremanager

import com.example.weatherapp.data.datastore.DataStoreManager
import com.example.weatherapp.data.datastore.IDataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//class FakeDataStoreManager : DataStoreManager {
//    var savedLanguage: String? = null
//    var savedLatitude: Double? = null
//    var savedLongitude: Double? = null
//
//     suspend fun saveLanguage(language: String) {
//        savedLanguage = language
//    }
//
//     suspend fun saveLocation(latitude: Double, longitude: Double) {
//        savedLatitude = latitude
//        savedLongitude = longitude
//    }
//
//}
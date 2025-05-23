package com.example.weatherapp.data.sealedclasses

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.weatherapp.R
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomBarRoutes(
   val route: String, val title: Int, val icon: ImageVector
) {
    @Serializable
    object Home : BottomBarRoutes("ToHome/{lat}/{long}",R.string.home, Icons.Filled.Home)

    @Serializable
    object Favourites :
        BottomBarRoutes("ToFavorites",R.string.favourites,  Icons.Filled.Favorite)

    @Serializable
    object Alarms :
        BottomBarRoutes("ToAlarms",R.string.alarms,  Icons.Filled.Notifications)

    @Serializable
    object Settings :
        BottomBarRoutes("ToSettings",R.string.settings,  Icons.Filled.Settings)

//    @Serializable
//    object Details :
//        BottomBarRoutes("details/{lat}/{long}",R.string.settings,  Icons.Filled.Settings)
}
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
   val route: String, val title: String, val icon: ImageVector
) {
    @Serializable
    object Home : BottomBarRoutes("ToHome","Home", Icons.Filled.Home)

    @Serializable
    object Favourites :
        BottomBarRoutes("ToFavorites","Favorites",  Icons.Filled.Favorite)

    @Serializable
    object Alarms :
        BottomBarRoutes("ToAlarms","Alarms",  Icons.Filled.Notifications)

    @Serializable
    object Settings :
        BottomBarRoutes("ToSettings","Settings",  Icons.Filled.Settings)
}
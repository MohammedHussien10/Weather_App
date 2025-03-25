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
    object Home : BottomBarRoutes("ToHome",R.string.home, Icons.Filled.Home)

    @Serializable
    object Favourites :
        BottomBarRoutes("ToFavorites",R.string.home,  Icons.Filled.Favorite)

    @Serializable
    object Alarms :
        BottomBarRoutes("ToAlarms",R.string.home,  Icons.Filled.Notifications)

    @Serializable
    object Settings :
        BottomBarRoutes("ToSettings",R.string.home,  Icons.Filled.Settings)
}
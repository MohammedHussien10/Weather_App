package com.example.weatherapp.data.remote


import java.text.SimpleDateFormat
import java.util.*

fun convertTimestampToDate(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
    return format.format(date)
}

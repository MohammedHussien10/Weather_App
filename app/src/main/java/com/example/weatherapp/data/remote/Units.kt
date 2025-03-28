package com.example.weatherapp.data.remote


import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

//Date and Time
fun convertTimestampToDate(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
    return format.format(date)

}

//DateOnly
fun convertTimestampToDateOnly(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("EEE, dd MMM", Locale.getDefault()) // EEE = Fri, MMM = Feb
    return format.format(date)
}


//TimeOnly
fun convertTimestampToTimeOnly(timestamp: Long): String {
    val calendar = Calendar.getInstance().apply { timeInMillis = timestamp * 1000 }
    val hour = calendar.get(Calendar.HOUR)
    val minute = calendar.get(Calendar.MINUTE)
    val amPm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"

    return String.format("%02d:%02d %s", if (hour == 0) 12 else hour, minute, amPm)
}

//Forecast get next 5 days
fun getNextFiveDaysForecast(forecast: ForecastResponse): List<ForecastItem> {
    val groupedByDate = forecast.list.groupBy { convertTimestampToDay(it.dt) }
    return groupedByDate.values.map { it.first() }.take(5)
}

//convert Timestamp to Day
fun convertTimestampToDay(timestamp: Long): String {
    val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
    val date = Date(timestamp * 1000)
    return sdf.format(date)
}
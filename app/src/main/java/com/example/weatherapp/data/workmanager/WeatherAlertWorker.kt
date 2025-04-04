package com.example.weatherapp.data.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.weatherapp.R
import com.example.weatherapp.data.datastore.DataStoreManager
import com.example.weatherapp.data.enumclasses.AlertType
import java.util.Calendar
import java.util.concurrent.TimeUnit

class WeatherAlertWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {


    object WorkManagerProvider {
        @Volatile
        private var instance: WorkManager? = null

        fun getInstance(context: Context): WorkManager {
            return instance ?: synchronized(this) {
                val newInstance = WorkManager.getInstance(context.applicationContext)
                instance = newInstance
                newInstance
            }
        }
    }
    override fun doWork(): Result {

        val alertType = AlertType.valueOf(inputData.getString("alertType") ?: AlertType.Notification.name)

        when (alertType) {
            AlertType.Notification -> showNotification()
            AlertType.AlarmSound -> playAlarmSound()  // يمكنك إضافة دالة لتشغيل الصوت إذا لزم الأمر
        }

        return Result.success()
    }

    private fun showNotification() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "weather_alert_channel",
                "Weather Alerts",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for weather alerts"
            }
            notificationManager.createNotificationChannel(channel)
        }


        val notification = NotificationCompat.Builder(applicationContext, "weather_alert_channel")
            .setContentTitle("Weather Alert")
            .setContentText("The weather alert is active.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun playAlarmSound() {

    }




}

fun createWeatherAlertWork(context: Context, hour: Int, minute: Int, alertType: AlertType) {

    val calendar = Calendar.getInstance()
    val currentTime = calendar.timeInMillis

    // تحديد الوقت الذي يريده المستخدم
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)

    val targetTime = calendar.timeInMillis
    val delay = if (targetTime > currentTime) {
        targetTime - currentTime
    } else {
        // إذا كان الوقت المستهدف في الماضي، يمكن تعيينه في اليوم التالي
        targetTime + TimeUnit.DAYS.toMillis(1) - currentTime
    }

    // جدولة العمل عبر WorkManager
    val workRequest = OneTimeWorkRequestBuilder<WeatherAlertWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setInputData(workDataOf("alertType" to alertType.name))
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}


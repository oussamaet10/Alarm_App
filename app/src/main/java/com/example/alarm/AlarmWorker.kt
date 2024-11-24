package com.example.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.alarm.MainActivity.Companion.EXTRA_ALARM_TITLE

class AlarmWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Retrieve the alarm title from input data
        val alarmTitle = inputData.getString(EXTRA_ALARM_TITLE) ?: "Alarm"

        // Log the alarm trigger for debugging
        Log.d("AlarmWorker", "Alarm triggered: $alarmTitle")

        // Show the notification
        showNotification(alarmTitle)

        // Start the music service
        val musicIntent = Intent(applicationContext, MusicService::class.java)
        applicationContext.startService(musicIntent)

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

    private fun showNotification(title: String) {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Create a notification manager
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for Android O and above
        val channelId = "alarm_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Alarm Notifications"
            val channelDescription = "Channel for alarm notifications"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, channelImportance).apply {
                description = channelDescription
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_alarm) // Replace with your app's notification icon
            .setContentTitle("Alarm")
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Show the notification
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
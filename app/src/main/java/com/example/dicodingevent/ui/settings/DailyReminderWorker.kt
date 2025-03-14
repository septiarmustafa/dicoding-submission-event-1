package com.example.dicodingevent.ui.settings

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dicodingevent.R
import com.example.dicodingevent.data.repository.EventRepository
import com.example.dicodingevent.data.remote.response.Result as Res
import com.example.dicodingevent.di.AppContainer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DailyReminderWorker(
    appContext: Context, workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val eventRepository: EventRepository = AppContainer(appContext).eventRepository

    override suspend fun doWork(): Result {
        val sharedPreferences = applicationContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isReminderEnabled = sharedPreferences.getBoolean("daily_reminder_enabled", false)

        if (!isReminderEnabled) {
            return Result.success()
        }

        return try {
            val eventResult = runBlocking { eventRepository.getNearestActiveEvent().first() }
            when (eventResult) {
                is Res.Success -> {
                    val event = eventResult.data
                    if (event != null) {
                        sendNotification(event.name ?: "Unknown Event", event.beginTime ?: "Unknown Time")
                        Result.success()
                    } else {
                        Result.failure()
                    }
                }
                is Res.Error -> {
                    Result.failure()
                }
                else -> Result.failure()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun sendNotification(eventName: String, eventTime: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "daily_reminder_channel"
        val channelName = "Daily Reminder"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_event_note_24)
            .setContentTitle(eventName)
            .setContentText("Recommendation event for you at $eventTime")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}

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
import com.example.dicodingevent.data.repository.SettingsRepository
import com.example.dicodingevent.di.AppContainer
import com.example.dicodingevent.shared.DateUtils
import kotlinx.coroutines.flow.first
import kotlin.Exception
import kotlin.String
import com.example.dicodingevent.data.remote.response.Result as Res

class DailyReminderWorker(
    appContext: Context, workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val settingsRepository: SettingsRepository = SettingsRepository(appContext)
    private val eventRepository: EventRepository = AppContainer(appContext).eventRepository

    override suspend fun doWork(): Result {
        val isReminderEnabled = settingsRepository.reminderSetting.first()

        if (!isReminderEnabled) {
            return Result.success()
        }

        try {
            var result: Result = Result.success()

            eventRepository.getNearestActiveEvent().collect { eventResult ->

                when (eventResult) {
                    is Res.Loading -> {
                        result = Result.retry()
                    }

                    is Res.Success -> {
                        val event = eventResult.data
                        if (event != null) {
                            sendNotification(
                                event.name ?: "Unknown Event",
                                DateUtils.formatToEn(event.beginTime ?: "")
                            )
                            result = Result.success()
                        } else {
                            result = Result.failure()
                        }
                    }

                    is Res.Error -> {
                        result = Result.failure()
                    }
                }
            }

            return result
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    private fun sendNotification(eventName: String, eventTime: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = CHANNEL_ID
        val channelName = CHANNEL_NAME

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
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

    companion object {
        const val CHANNEL_ID = "DAILY_REMINDER_CHANNEL"
        const val CHANNEL_NAME = "Daily Reminder"
    }
}

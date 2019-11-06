package com.system.sound.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.system.sound.R
import com.system.sound.informations.Audio
import javax.inject.Inject

class NotificationHelper @Inject constructor(private val mContext: Context) {

    private val CHANNEL_ID = "com.my.sound.service.662016"
    private val CHANNEL_NAME = "com.my.sound.service"
    private val NOTIFICATION_ID = 662016

    private var notificationBuilder: NotificationCompat.Builder
    private var notification: Notification

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)

            val notificationManager =
                getSystemService(mContext, NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }

        notificationBuilder = NotificationCompat.Builder(mContext, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_dashboard_black_24dp)
            .setWhen(System.currentTimeMillis())
            .setContentTitle("Player")
            .setContentText("is running ...")
            .setPriority(NotificationCompat.PRIORITY_HIGH)


        notification = notificationBuilder.build()

    }

    fun showNotification(@Nullable song: Audio? = null, title: String, @Nullable notificationId: Int? = null) =
        when (notificationId) {
            null -> {
                notificationBuilder.setContentText(song?.title ?: title)
                with(NotificationManagerCompat.from(mContext)) {
                    notify(NOTIFICATION_ID, notification)
                }
            }
            else -> {
                notificationBuilder.setContentText(title)
                with(NotificationManagerCompat.from(mContext)) {
                    notify(notificationId, notification)
                }
            }
        }


    fun hideNotification(@Nullable notificationId: Int? = null) =
        when (notificationId) {
            null -> {
                with(NotificationManagerCompat.from(mContext)) {
                    cancel(NOTIFICATION_ID)
                }
            }
            else -> {
                with(NotificationManagerCompat.from(mContext)) {
                    cancel(notificationId)
                }
            }
        }


}
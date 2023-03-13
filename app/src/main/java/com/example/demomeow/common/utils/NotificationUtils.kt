package com.example.demomeow.common.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.demomeow.presentation.ui.main.MainActivity
import com.example.demomeow.R
import com.example.demomeow.di.App

object NotificationUtils {

    /**
     * This is the method to create notification channel
     */
    private fun createNotificationChannel(
        context: Context,
        channelId: String,
        notificationManager: NotificationManager
    ) {
        //if you have already setup notification Channels then do nothing
        if (notificationManager.notificationChannels.any { it.id == channelId }) {
            return
        }
        //setup notification Channels
        val name = App.context.getString(R.string.notification_channel_name)
        val description = App.context.getString(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, name, importance)

        channel.apply {
            if (channelId == App.context.getString(R.string.default_notification_channel)) {
                this.description = description
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            } else {
                setSound(null, null)
                enableLights(false)
                enableVibration(false)
            }

        }
    }

    /**
     * This is the method to create notification for remind users
     */
    fun createNotification(context: Context, body: String?): Notification {
        val channelId = context.getString(R.string.default_notification_channel)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, MainActivity::class.java)

        val contentIntent =
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            /**
             * Set the small icon to use in the notification layouts.  Different classes of devices
             * may return different sizes.  See the UX guidelines for more information on how to
             * design these icons.
             */
            .setSmallIcon(R.mipmap.ic_launcher)
            // Set the text (second row) of the notification, in a standard notification.
            .setContentText(body)
            /**
             * Setting this flag will make it so the notification is automatically
             * canceled when the user clicks it in the panel.  The PendingIntent
             * set with setDeleteIntent will be broadcast when the notification
             * is canceled.
             */
            .setAutoCancel(true)
            /**
             * Supply a PendingIntentto send when the notification is clicked.
             * If you do not supply an intent, you can now add PendingIntents to individual
             * views to be launched when clicked by calling {RemoteViews#setOnClickPendingIntent
             * RemoteViews.setOnClickPendingIntent(int,PendingIntent)}.
             */
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setDefaults(Notification.DEFAULT_VIBRATE)
            /**
             * Set the relative priority for this notification.
             * Priority is an indication of how much of the user's
             * valuable attention should be consumed by this
             * notification. Low-priority notifications may be hidden from
             * the user in certain situations, while the user might be
             * interrupted for a higher-priority notification.
             * The system sets a notification's priority based on various factors including the
             * setPriority value. The effect may differ slightly on different platforms.
             */
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        createNotificationChannel(context, channelId, notificationManager)
        val notification = notificationBuilder.build()
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        return notification

    }
}

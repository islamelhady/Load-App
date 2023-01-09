package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

// Notification ID.
private val NOTIFICATION_ID = 0

fun NotificationManager.sendDownloadCompleted(messageBody: String, applicationContext: Context) {

    val detailIntent = Intent(applicationContext, DetailActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(applicationContext, NOTIFICATION_ID, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    // Build notification
    val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.channel_id))
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(messageBody)
        .setContentText(applicationContext.getString(R.string.app_description))
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .addAction(R.drawable.ic_assistant_black_24dp, applicationContext.getString(R.string.notification_button), contentPendingIntent)

    notify(NOTIFICATION_ID, builder.build())


}
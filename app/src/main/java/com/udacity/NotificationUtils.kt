package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat

// Notification ID.
private val NOTIFICATION_ID = 0

fun NotificationManager.sendDownloadCompleted(fileName: String, fileDescription: String, status: String, applicationContext: Context) {
    val extras = Bundle()
    extras.putString(applicationContext.getString(R.string.key_file_name), fileName)
    extras.putString(applicationContext.getString(R.string.key_description), fileDescription)
    extras.putString(applicationContext.getString(R.string.key_status), status)

    val detailIntent = Intent(applicationContext, DetailActivity::class.java)
    detailIntent.putExtras(extras)

    val contentPendingIntent = PendingIntent.getActivity(applicationContext, NOTIFICATION_ID, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    // Build notification
    val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.channel_id))
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(fileName)
        .setContentText(fileDescription)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .addAction(R.drawable.ic_assistant_black_24dp, applicationContext.getString(R.string.action_button), contentPendingIntent)

    notify(NOTIFICATION_ID, builder.build())


}
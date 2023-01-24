package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(getString(R.string.channel_id), getString(R.string.channel_name))


        customButton.setOnClickListener {
            customButton.buttonState = ButtonState.Clicked
            val urlDownload = when (downloadGroup.checkedRadioButtonId) {
                glideRadioButton.id -> GLIDE_URL
                loadRadioButton.id -> LOAD_URL
                retrofitRadioButton.id -> RETROFIT_URL
                else -> null
            }
            download(urlDownload)
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            customButton.buttonState = ButtonState.Completed
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val query = DownloadManager.Query()
                query.setFilterById(id)

                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    val status =
                        when (cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))) {
                            DownloadManager.STATUS_SUCCESSFUL -> getString(R.string.success)
                            DownloadManager.STATUS_FAILED -> getString(R.string.failed)
                            else -> getString(R.string.unknown)
                        }
                    val fileName = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TITLE))
                    val fileDescription = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_DESCRIPTION))
                    notificationManager.sendDownloadCompleted(fileName, fileDescription, status, applicationContext)
                }
            }
        }
    }

    private fun download(urlDownload: String?) {
        if (urlDownload != null) {
            val request =
                DownloadManager.Request(Uri.parse(urlDownload))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            when (urlDownload) {
                LOAD_URL -> {
                    request.setTitle(getString(R.string.load))
                    request.setDescription(getString(R.string.load_app_description))
                }
                GLIDE_URL -> {
                    request.setTitle(getString(R.string.glide))
                    request.setDescription(getString(R.string.glide_description))
                }
                RETROFIT_URL -> {
                    request.setTitle(getString(R.string.retrofit))
                    request.setDescription(getString(R.string.retrofit_description))
                }
            }

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        } else Toast.makeText(applicationContext, getString(R.string.select_one), Toast.LENGTH_SHORT).show()
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, channelName,
                //  change importance
                NotificationManager.IMPORTANCE_HIGH
            )// disable badges for this channel
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_channel_description)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

    companion object {
        private const val GLIDE_URL =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val LOAD_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val RETROFIT_URL =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
    }

}

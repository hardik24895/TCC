package com.tcc.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.tcc.app.R
import com.tcc.app.activity.HomeActivity
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Logger
import com.tcc.app.utils.SessionManager

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var session: SessionManager

    private companion object {
        private const val TAG = "Notification"
    }

    override fun onCreate() {
        super.onCreate()
        session = SessionManager(context = applicationContext)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Logger.e(TAG, "From: " + remoteMessage.from)

        println("data:--- " + Gson().toJson(remoteMessage.data))
        println("notification:-- " + Gson().toJson(remoteMessage.notification))

        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["title"]
            val message = remoteMessage.data["message"]

            Logger.e(TAG, "Message Notification Data: " + remoteMessage.data)
            if (session.isLoggedIn) buildNotification(title, message, remoteMessage.data)
        } else if (remoteMessage.notification != null) {
            val title = remoteMessage.notification?.title
            val message = remoteMessage.notification?.body

            Logger.e(TAG, "Message Notification: " + remoteMessage.notification)
            if (session.isLoggedIn) buildNotification(title, message, null)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Logger.e(TAG, "Refreshed token: $token")
    }

    private fun buildNotification(
        title: String?,
        message: String?,
        data: MutableMap<String, String>?
    ) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        if (data != null) {
            val bundle = Bundle()
            //  val userId = data["userId"]
            if (title != null) bundle.putString(Constant.TITLE, title)
            intent.putExtra(Constant.DATA, bundle)
        }

        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(HomeActivity::class.java)
        stackBuilder.addNextIntent(intent)

        val contentIntent = stackBuilder.getPendingIntent(
            System.currentTimeMillis().toInt(),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_CANCEL_CURRENT
        )
        val channelId = "Default"
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.new_icon)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(contentIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        manager.notify(System.currentTimeMillis().toInt(), builder.build())
    }


}
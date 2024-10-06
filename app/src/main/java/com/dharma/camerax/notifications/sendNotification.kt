package com.dharma.camerax.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dharma.camerax.MainActivity
import com.dharma.camerax.R
import com.dharma.camerax.app.CameraXApp
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun sendNotification(
    applicationContext : Context,
    destinationActivity: Class<*> = MainActivity::class.java,
    title: String, message: String) {

        // Create an Intent to navigate to a specific screen or perform an action
        val intent = Intent(applicationContext, destinationActivity).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(applicationContext, CameraXApp.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // replace with your icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(1, builder.build())
    }

fun isNotificationPermissionGranted(context: Context): Boolean {
    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
        return ContextCompat.checkSelfPermission(context,
            Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }
    return true
}
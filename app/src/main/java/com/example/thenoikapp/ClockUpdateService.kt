package com.example.thenoikapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ClockUpdateService : Service() {

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate() {
        super.onCreate()

        // Notifica obbligatoria per il foreground service
        val channelId = "widget_update_channel"
        val channelName = "Widget Update Service"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(chan)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Clock Widget Active")
            .setContentText("Keeps updated the nclock")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(1, notification)

        // Programma aggiornamenti ogni minuto
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, WidgetReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val firstTrigger = System.currentTimeMillis() + 1000
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            firstTrigger,
            60000L,
            pendingIntent
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmManager.cancel(pendingIntent)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

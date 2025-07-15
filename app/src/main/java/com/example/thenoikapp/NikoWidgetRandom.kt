package com.example.thenoikapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.RemoteViews
import kotlin.random.Random

class RandomImageWidget : AppWidgetProvider() {

    private val imageIds = arrayOf(
        R.drawable.img1,
        R.drawable.img2,
        R.drawable.img3,
        R.drawable.img4,
        R.drawable.img5,
        R.drawable.img6
    )

    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        updateAllWidgets(context, manager, ids)
        scheduleNextUpdate(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(ComponentName(context, RandomImageWidget::class.java))

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            "com.example.thenoikapp.UPDATE_RANDOM_IMAGE" -> {
                updateAllWidgets(context, manager, ids)
                scheduleNextUpdate(context)
            }
        }
    }

    private fun updateAllWidgets(context: Context, manager: AppWidgetManager, ids: IntArray) {
        for (id in ids) {
            val views = RemoteViews(context.packageName, R.layout.widget_random_image)
            val randomRes = imageIds.random()
            views.setImageViewResource(R.id.random_image_view, randomRes)
            manager.updateAppWidget(id, views)
        }
    }

    private fun scheduleNextUpdate(context: Context) {
        val intent = Intent(context, RandomImageWidget::class.java).apply {
            action = "com.example.thenoikapp.UPDATE_RANDOM_IMAGE"
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val interval = AlarmManager.INTERVAL_DAY

        alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + interval,
            pendingIntent)

        /* This required alarmManager.canScheduleExactAlarms(), which is only for API 31 (we have 26)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + interval,
            pendingIntent)
        )*/
    }
}

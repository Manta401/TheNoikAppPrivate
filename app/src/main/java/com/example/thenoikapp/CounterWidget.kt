package com.example.thenoikapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class CounterWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        for (id in ids) {
            updateWidget(context, manager, id)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == "com.example.thenoikapp.INCREMENT") {
            val prefs = context.getSharedPreferences("counter_prefs", Context.MODE_PRIVATE)
            val current = prefs.getInt("counter", 0)
            prefs.edit().putInt("counter", current + 1).apply()

            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, CounterWidget::class.java))
            for (id in ids) {
                updateWidget(context, manager, id)
            }
        }
    }

    private fun updateWidget(context: Context, manager: AppWidgetManager, id: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_counter)

        val prefs = context.getSharedPreferences("counter_prefs", Context.MODE_PRIVATE)
        val count = prefs.getInt("counter", 0)
        views.setTextViewText(R.id.counter_text, count.toString())

        val intent = Intent(context, CounterWidget::class.java).apply {
            action = "com.example.thenoikapp.INCREMENT"
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.increment_image, pendingIntent)

        manager.updateAppWidget(id, views)
    }
}

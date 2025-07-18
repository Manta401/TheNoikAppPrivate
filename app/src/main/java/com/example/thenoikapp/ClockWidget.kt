package com.example.thenoikapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.SystemClock


class ClockWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // Aggiorna tutti i widget (Update all widgets)
        WidgetUpdater.updateAll(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            "com.example.thenoikapp.TOGGLE_TIME_FORMAT" -> {
                val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
                val currentFormat = prefs.getBoolean("is_24h", true)
                prefs.edit().putBoolean("is_24h", !currentFormat).apply()
            }
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_SCREEN_ON -> {
                // Questi eventi richiedono un aggiornamento del widget (These events require an update of the widget)
            }
        }

        // Aggiorna comunque il widget (Update the widget anyway.)
        WidgetUpdater.updateAll(context)
    }

    override fun onEnabled(context: Context) {
        WidgetUpdater.updateAll(context)
    }

    override fun onDisabled(context: Context) {
    }

}


package com.example.thenoikapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

// i translated the comments to english with google translate so i can understand them :3
// - wa1kingchee5e


class WidgetReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d("WidgetReceiver", "Received action: $action")

        // Gestire il formato orario (Manage the time format)
        if (action == "com.example.thenoikapp.TOGGLE_TIME_FORMAT") {
            val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
            val current = prefs.getBoolean("is_24h", true)
            prefs.edit().putBoolean("is_24h", !current).apply()
        }

        // Azioni di aggiornamento widget (Widget update actions)
        if (action == "com.example.thenoikapp.TOGGLE_TIME_FORMAT" ||
            action == Intent.ACTION_SCREEN_ON ||
            action == Intent.ACTION_TIME_CHANGED ||
            action == Intent.ACTION_TIMEZONE_CHANGED ||
            action == Intent.ACTION_BOOT_COMPLETED ||
            action == "android.appwidget.action.APPWIDGET_UPDATE" ||
            action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS ||
            action == "android.appwidget.action.ACTION_TIME_CHANGED" ||
            action == "android.appwidget.action.ACTION_TIME_TICK"
            // Questa è l'azione per il tasto Home (This is the action for the Home button)
        ) {
            if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
                val reason = intent.getStringExtra("reason")
                if (reason != null && reason == "homekey") {
                    Log.d("WidgetReceiver", "Home button pressed!")
                    // Esegui l'aggiornamento del widget quando il tasto Home viene premuto (Refresh the widget when the Home button is pressed)
                    WidgetUpdater.updateAll(context)  // Aggiorna il widget (update the widget)
                }
            }

            // Assicurati che il widget venga aggiornato anche in altri casi (Make sure the widget is updated in other cases as well)
            WidgetUpdater.updateAll(context)
        }
    }
}

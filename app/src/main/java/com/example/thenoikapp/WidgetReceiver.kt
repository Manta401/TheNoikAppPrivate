package com.example.thenoikapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
/*
class WidgetReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d("WidgetReceiver", "Received action: $action")

        // Gestire il formato orario
        if (action == "com.example.thenoikapp.TOGGLE_TIME_FORMAT") {
            val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
            val current = prefs.getBoolean("is_24h", true)
            prefs.edit().putBoolean("is_24h", !current).apply()
        }

        // Azioni di aggiornamento widget
        if (action == "com.example.thenoikapp.TOGGLE_TIME_FORMAT" ||
            action == Intent.ACTION_SCREEN_ON ||
            action == Intent.ACTION_TIME_CHANGED ||
            action == Intent.ACTION_TIMEZONE_CHANGED ||
            action == Intent.ACTION_BOOT_COMPLETED ||
            action == "android.appwidget.action.APPWIDGET_UPDATE" ||
            action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS // Questa è l'azione per il tasto Home
        ) {
            if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
                val reason = intent.getStringExtra("reason")
                if (reason != null && reason == "homekey") {
                    Log.d("WidgetReceiver", "Home button pressed!")
                    // Esegui l'aggiornamento del widget quando il tasto Home viene premuto
                    WidgetUpdater.updateAll(context)  // Aggiorna il widget
                }
            }

            // Assicurati che il widget venga aggiornato anche in altri casi
            WidgetUpdater.updateAll(context)
        }
    }
}
*/
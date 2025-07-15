package com.example.thenoikapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ClockReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Debug
        /*val text = intent.toString()
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(context, text, duration)
        toast.show()*/

        WidgetUpdater.updateAll(context)

    }
}
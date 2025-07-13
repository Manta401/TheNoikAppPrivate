package com.example.thenoikapp

import android.content.Context
import android.net.Uri

object ImageStorage {
    fun saveImageUri(context: Context, widgetId: Int, uri: Uri) {
        val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("img_$widgetId", uri.toString()).apply()
    }

    fun loadImageUri(context: Context, widgetId: Int): Uri? {
        val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        val uriStr = prefs.getString("img_$widgetId", null)
        return uriStr?.let { Uri.parse(it) }
    }
}

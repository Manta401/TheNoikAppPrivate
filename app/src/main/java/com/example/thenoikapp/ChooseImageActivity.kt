package com.example.thenoikapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.RemoteViews

class CustomImageWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_image_custom)

            val intent = Intent(context, ChooseImageActivity::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            views.setOnClickPendingIntent(R.id.widget_image, pendingIntent)

            val uri = ImageStorage.loadImageUri(context, appWidgetId)
            if (uri != null) {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                views.setImageViewBitmap(R.id.widget_image, bitmap)

                views.setInt(R.id.widget_image, "setBackgroundResource", android.R.color.transparent)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

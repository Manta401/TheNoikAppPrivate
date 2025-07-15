package com.example.thenoikapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.util.TypedValue
import android.widget.RemoteViews
import androidx.core.content.res.ResourcesCompat
import java.text.SimpleDateFormat
import java.util.*


object WidgetUpdater {
    fun updateAll(context: Context) {
        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(ComponentName(context, ClockWidget::class.java))

        val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        val is24h = prefs.getBoolean("is_24h", true)


        val defaultLocale = Locale.getDefault()
        val timeFormat = SimpleDateFormat(if (is24h) "HH:mm" else "hh:mm a", defaultLocale) // don't forget to change from seconds back to "HH:mm"
        val dateFormat = SimpleDateFormat("MM/dd", defaultLocale)
        val dayFormat = SimpleDateFormat("EEE", defaultLocale)
        val calendar = Calendar.getInstance()
        val time = timeFormat.format(calendar.time)
        val date = dateFormat.format(calendar.time)
        val day = dayFormat.format(calendar.time)

        for (id in ids) {
            val views = RemoteViews(context.packageName, R.layout.widget_clock)

            val bitmapTime = createBitmapWithFont(context, time, 76f)
            val bitmapDate = createBitmapWithFont(context, date, 30f)
            val bitmapDay = createBitmapWithFont(context, day, 30f)

            views.setImageViewBitmap(R.id.time_image, bitmapTime)
            views.setImageViewBitmap(R.id.date_image, bitmapDate)
            views.setImageViewBitmap(R.id.day_image, bitmapDay)

            val intent = Intent(context, ClockWidget::class.java).apply {
                action = "com.example.thenoikapp.TOGGLE_TIME_FORMAT"
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.date_container, pendingIntent)

            manager.updateAppWidget(id, views)
        }


    }

    fun createBitmapWithFont(context: Context, text: String, sizeSp: Float): Bitmap {
        val displayMetrics = context.resources.displayMetrics
        val textSizePx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sizeSp, displayMetrics)

        val typeface = ResourcesCompat.getFont(context, R.font.komika_axis)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = textSizePx
            this.typeface = typeface
        }

        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val width = bounds.width() + 40
        val height = bounds.height() + 40

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val x = 20f
        val y = bounds.height().toFloat() + 20f

        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        paint.strokeWidth = 6f
        canvas.drawText(text, x, y, paint)

        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        canvas.drawText(text, x, y, paint)

        return bitmap
    }

    fun cancel(context: Context) {
        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(ComponentName(context, ClockWidget::class.java))

        // You can perform some cleanup or cancel the widget update here, for example:
        for (id in ids) {
            val views = RemoteViews(context.packageName, R.layout.widget_clock)

            // Clear out any view updates (optional)
            views.setImageViewBitmap(R.id.time_image, null)
            views.setImageViewBitmap(R.id.date_image, null)
            views.setImageViewBitmap(R.id.day_image, null)

            // Update the widget to reset it
            manager.updateAppWidget(id, views)
        }
    }
}

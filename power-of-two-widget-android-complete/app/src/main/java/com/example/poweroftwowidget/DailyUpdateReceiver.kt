package com.example.poweroftwowidget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import java.math.BigInteger
import java.util.*

class DailyUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        // Debug log to confirm alarm triggered
        Log.d("DailyUpdateReceiver", "Alarm triggered!")

        // Optional user feedback (not always visible if app is backgrounded)
        Toast.makeText(context, "Alarm triggered!", Toast.LENGTH_SHORT).show()

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetComponent = ComponentName(context, PowerOfTwoWidgetProvider::class.java)
        val widgetIds = appWidgetManager.getAppWidgetIds(widgetComponent)

        val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)

        // Get the stored install time
        val installTime = prefs.getLong("install_time", System.currentTimeMillis())

        // Normalize install time to midnight
        val startDay = Calendar.getInstance().apply {
            timeInMillis = installTime
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Normalize current time to midnight
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Days passed since install
        val daysPassed = ((today.timeInMillis - startDay.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()

        // Starting number from preferences
        val startN = prefs.getInt("start_number", 0)

        // Compute 2^(start + days)
        val powerValue = startN + daysPassed
        val result = BigInteger.TWO.pow(powerValue).toString()

        Log.d("DailyUpdateReceiver", "Updating widget with 2^$powerValue = $result")

        // Update each widget instance
        for (id in widgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            views.setTextViewText(R.id.widget_text, "2^$powerValue = $result")
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}

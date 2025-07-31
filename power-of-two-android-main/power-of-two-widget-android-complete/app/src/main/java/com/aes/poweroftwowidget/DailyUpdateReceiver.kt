package com.aes.powerwidget
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.widget.RemoteViews
import android.widget.Toast
import java.math.BigInteger
import java.util.*


class DailyUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetComponent = ComponentName(context, PowerOfTwoWidgetProvider::class.java)
        val widgetIds = appWidgetManager.getAppWidgetIds(widgetComponent)

        val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        val installTime = prefs.getLong("install_time", System.currentTimeMillis())

        val startDay = Calendar.getInstance().apply {
            timeInMillis = installTime
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val daysPassed = ((today.timeInMillis - startDay.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
        val startN = prefs.getInt("start_number", 0)
        val result = BigInteger.TWO.pow(startN + daysPassed).toString()

        for (id in widgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            views.setTextViewText(R.id.widget_text, "2^${startN + daysPassed} = $result")
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}

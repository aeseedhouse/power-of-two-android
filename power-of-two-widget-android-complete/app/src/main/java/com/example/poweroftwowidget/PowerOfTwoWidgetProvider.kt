package com.aes.poweroftwowidget


import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.math.BigInteger
import java.util.*

class PowerOfTwoWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val sharedPreferences = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)

        // Store install date once
        val installKey = "install_time"
        var installTime = sharedPreferences.getLong(installKey, -1L)
        if (installTime == -1L) {
            installTime = System.currentTimeMillis()
            sharedPreferences.edit().putLong(installKey, installTime).apply()
        }

        // Zero out time and calculate days passed
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

        // Get saved starting number
        val startN = sharedPreferences.getInt("start_number", 0)
        val n = startN + daysPassed
        val result = BigInteger.TWO.pow(n).toString()

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            views.setTextViewText(R.id.widget_text, "2^$n = $result")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    // Optional: Trigger manual updates
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            val manager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, PowerOfTwoWidgetProvider::class.java)
            val ids = manager.getAppWidgetIds(componentName)
            onUpdate(context, manager, ids)
        }
    }
}

package com.example.poweroftwowidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import java.math.BigInteger
import java.util.*

class PowerOfTwoWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val startDay = Calendar.getInstance().apply {
            set(2024, Calendar.JANUARY, 1)  // Customize start date here
        }

        val today = Calendar.getInstance()
        val daysPassed = ((today.timeInMillis - startDay.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
        val n = 10 + daysPassed  // Change 10 to your preferred base number

        val result = BigInteger.TWO.pow(n).toString()

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            views.setTextViewText(R.id.widget_text, result)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

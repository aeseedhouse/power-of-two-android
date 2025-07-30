package com.example.poweroftwowidget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.app.AlarmManager
import android.app.PendingIntent
import android.util.Log
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputField = findViewById<EditText>(R.id.inputNumber)
        val saveButton = findViewById<Button>(R.id.saveButton)

        val sharedPrefs = getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        inputField.setText(sharedPrefs.getInt("start_number", 10).toString())

        saveButton.setOnClickListener {
            val input = inputField.text.toString().toIntOrNull() ?: 10
            sharedPrefs.edit().putInt("start_number", input).apply()
            sharedPrefs.edit().putLong("install_time", System.currentTimeMillis()).apply()

            // Trigger widget update immediately
            val updateIntent = Intent(this, PowerOfTwoWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(
                    ComponentName(application, PowerOfTwoWidgetProvider::class.java)
                )
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            }
            sendBroadcast(updateIntent)

            // Schedule alarm 1 minute from now
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent("com.example.poweroftwowidget.DAILY_UPDATE").apply {
                setPackage(packageName)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                alarmIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val triggerTime = System.currentTimeMillis() + 60_000  // 1 minute from now

            Log.d("MainActivity", "Setting alarm for 1 minute from now: ${Date(triggerTime)}")

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )

            // COMMENTED OUT FOR TESTING — Un-comment for daily updates
            /*
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 12)
                set(Calendar.MINUTE, 5)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (before(Calendar.getInstance())) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
            */

            finish()
        }
    }
}

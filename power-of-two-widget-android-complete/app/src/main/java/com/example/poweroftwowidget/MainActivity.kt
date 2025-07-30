package com.example.poweroftwowidget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

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

            sharedPrefs.edit()
                .putInt("start_number", input)
                .putLong("install_time", System.currentTimeMillis())
                .apply()

            // Manually trigger widget update
            val intent = Intent(this, PowerOfTwoWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(
                    ComponentName(application, PowerOfTwoWidgetProvider::class.java)
                )
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            }
            sendBroadcast(intent)

            finish()
        }
    }
}

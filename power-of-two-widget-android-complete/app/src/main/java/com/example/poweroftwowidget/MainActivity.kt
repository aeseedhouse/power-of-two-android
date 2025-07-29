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

        saveButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val input = inputField.text.toString().toIntOrNull() ?: 10
            editor.putInt("start_number", input)
            editor.apply()

            // Trigger widget update
            val intent = Intent(this, PowerOfTwoWidgetProvider::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(
                ComponentName(application, PowerOfTwoWidgetProvider::class.java)
            )
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            sendBroadcast(intent)

            finish()
        }
    }
}

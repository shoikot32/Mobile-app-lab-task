package com.example.lab11sharedpreferences

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class SettingsViewerActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvTheme: TextView
    private lateinit var tvNotif: TextView
    private lateinit var tvLanguage: TextView
    private lateinit var tvFontSize: TextView
    private lateinit var tvLastSaved: TextView
    private lateinit var tvNoSettings: TextView
    private lateinit var btnEdit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_viewer)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Saved Settings"

        tvName = findViewById(R.id.tvName)
        tvTheme = findViewById(R.id.tvTheme)
        tvNotif = findViewById(R.id.tvNotif)
        tvLanguage = findViewById(R.id.tvLanguage)
        tvFontSize = findViewById(R.id.tvFontSize)
        tvLastSaved = findViewById(R.id.tvLastSaved)
        tvNoSettings = findViewById(R.id.tvNoSettings)
        btnEdit = findViewById(R.id.btnEdit)

        displaySettings()

        btnEdit.setOnClickListener {
            finish()
        }
    }

    private fun displaySettings() {
        val prefs = getSharedPreferences(
            MainActivity.PREF_FILE,
            Context.MODE_PRIVATE
        )

        val lastSaved = prefs.getLong(MainActivity.KEY_LAST_SAVED, 0L)

        if (lastSaved == 0L) {
            tvNoSettings.visibility = android.view.View.VISIBLE
            return
        }

        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val date = dateFormat.format(Date(lastSaved))

        tvName.text = "Name: ${prefs.getString(MainActivity.KEY_STUDENT_NAME, "-")}"
        tvTheme.text = "Theme: ${prefs.getString(MainActivity.KEY_THEME, "light")}"
        tvNotif.text =
            "Notifications: ${if (prefs.getBoolean(MainActivity.KEY_NOTIFICATIONS, true)) "On" else "Off"}"
        tvLanguage.text = "Language: ${prefs.getString(MainActivity.KEY_LANGUAGE, "English")}"
        tvFontSize.text = "Font Size: ${prefs.getInt(MainActivity.KEY_FONT_SIZE, 16)}sp"
        tvLastSaved.text = "Last Saved: $date"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
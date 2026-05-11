package com.example.lab11sharedpreferences
import com.example.lab11sharedpreferences.ProfileActivity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var etStudentName: EditText
    private lateinit var rbLight: RadioButton
    private lateinit var rbDark: RadioButton
    private lateinit var rbSystem: RadioButton
    private lateinit var switchNotif: SwitchCompat
    private lateinit var spinnerLang: Spinner
    private lateinit var seekBarFont: SeekBar
    private lateinit var tvFontSize: TextView
    private lateinit var btnSave: Button
    private lateinit var btnReset: Button
    private lateinit var btnViewSettings: Button

    companion object {
        const val PREF_FILE = "AppSettings"
        const val KEY_THEME = "KEY_THEME"
        const val KEY_NOTIFICATIONS = "KEY_NOTIFICATIONS"
        const val KEY_LANGUAGE = "KEY_LANGUAGE"
        const val KEY_FONT_SIZE = "KEY_FONT_SIZE"
        const val KEY_LAST_SAVED = "KEY_LAST_SAVED"
        const val KEY_STUDENT_NAME = "KEY_STUDENT_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etStudentName = findViewById(R.id.etStudentName)
        rbLight = findViewById(R.id.rbLight)
        rbDark = findViewById(R.id.rbDark)
        rbSystem = findViewById(R.id.rbSystem)
        switchNotif = findViewById(R.id.switchNotif)
        spinnerLang = findViewById(R.id.spinnerLang)
        seekBarFont = findViewById(R.id.seekBarFont)
        tvFontSize = findViewById(R.id.tvFontSize)
        btnSave = findViewById(R.id.btnSave)
        btnReset = findViewById(R.id.btnReset)
        btnViewSettings = findViewById(R.id.btnViewSettings)

        val languages = listOf("English", "Bangla", "Arabic", "French")
        val langAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            languages
        )
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLang.adapter = langAdapter

        seekBarFont.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                val fontSize = progress + 12
                tvFontSize.text = "Font Size: ${fontSize}sp"
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        btnSave.setOnClickListener {
            saveSettings()
        }

        btnReset.setOnClickListener {
            resetSettings()
        }

        btnViewSettings.setOnClickListener {
            startActivity(Intent(this, SettingsViewerActivity::class.java))
        }

        findViewById<FloatingActionButton>(R.id.fabProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadSettingsIntoUI()
    }

    private fun loadSettingsIntoUI() {
        val prefs = getSharedPreferences(PREF_FILE, MODE_PRIVATE)

        etStudentName.setText(prefs.getString(KEY_STUDENT_NAME, ""))

        when (prefs.getString(KEY_THEME, "light")) {
            "light" -> rbLight.isChecked = true
            "dark" -> rbDark.isChecked = true
            "system" -> rbSystem.isChecked = true
        }

        switchNotif.isChecked = prefs.getBoolean(KEY_NOTIFICATIONS, true)

        val fontSize = prefs.getInt(KEY_FONT_SIZE, 16)
        seekBarFont.progress = fontSize - 12
        tvFontSize.text = "Font Size: ${fontSize}sp"

        val savedLang = prefs.getString(KEY_LANGUAGE, "English")
        val langList = listOf("English", "Bangla", "Arabic", "French")
        val langIndex = langList.indexOf(savedLang)
        if (langIndex >= 0) {
            spinnerLang.setSelection(langIndex)
        }
    }

    private fun saveSettings() {
        val name = etStudentName.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            return
        }

        val theme = when {
            rbLight.isChecked -> "light"
            rbDark.isChecked -> "dark"
            else -> "system"
        }

        val language = spinnerLang.selectedItem.toString()
        val fontSize = seekBarFont.progress + 12
        val notifOn = switchNotif.isChecked

        val prefs = getSharedPreferences(PREF_FILE, MODE_PRIVATE)

        with(prefs.edit()) {
            putString(KEY_STUDENT_NAME, name)
            putString(KEY_THEME, theme)
            putBoolean(KEY_NOTIFICATIONS, notifOn)
            putString(KEY_LANGUAGE, language)
            putInt(KEY_FONT_SIZE, fontSize)
            putLong(KEY_LAST_SAVED, System.currentTimeMillis())
            apply()
        }

        Toast.makeText(this, "Settings saved successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun resetSettings() {
        val prefs = getSharedPreferences(PREF_FILE, MODE_PRIVATE)
        prefs.edit().clear().apply()

        etStudentName.text.clear()
        rbLight.isChecked = true
        switchNotif.isChecked = true
        seekBarFont.progress = 4
        tvFontSize.text = "Font Size: 16sp"
        spinnerLang.setSelection(0)

        Toast.makeText(this, "Settings reset to default", Toast.LENGTH_SHORT).show()
    }
}
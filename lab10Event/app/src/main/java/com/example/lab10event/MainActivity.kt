package com.example.lab10event

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnBrowse: Button
    private lateinit var btnBookings: Button
    private lateinit var btnNotifications: Button
    private lateinit var btnProfile: Button
    private lateinit var btnRegisterFeatured: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnBrowse           = findViewById(R.id.btnBrowse)
        btnBookings         = findViewById(R.id.btnBookings)
        btnNotifications    = findViewById(R.id.btnNotifications)
        btnProfile          = findViewById(R.id.btnProfile)
        btnRegisterFeatured = findViewById(R.id.btnRegisterFeatured)

        btnBrowse.setOnClickListener {
            startActivity(Intent(this, EventsListActivity::class.java))
        }

        btnRegisterFeatured.setOnClickListener {
            val intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("event", SampleData.events[0])
            startActivity(intent)
        }

        btnBookings.setOnClickListener {
            Toast.makeText(this, "No bookings yet", Toast.LENGTH_SHORT).show()
        }

        btnNotifications.setOnClickListener {
            Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show()
        }

        btnProfile.setOnClickListener {
            Toast.makeText(this, "Profile coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
}
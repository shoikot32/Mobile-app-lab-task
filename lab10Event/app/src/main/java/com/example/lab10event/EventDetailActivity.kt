package com.example.lab10event

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EventDetailActivity : AppCompatActivity() {

    private lateinit var ivDetailBanner: ImageView
    private lateinit var tvDetailTitle: TextView
    private lateinit var tvDetailDate: TextView
    private lateinit var tvDetailVenue: TextView
    private lateinit var tvDetailDesc: TextView
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        ivDetailBanner = findViewById(R.id.ivDetailBanner)
        tvDetailTitle  = findViewById(R.id.tvDetailTitle)
        tvDetailDate   = findViewById(R.id.tvDetailDate)
        tvDetailVenue  = findViewById(R.id.tvDetailVenue)
        tvDetailDesc   = findViewById(R.id.tvDetailDesc)
        btnRegister    = findViewById(R.id.btnRegister)

        // Get event passed from previous screen
        val event = intent.getSerializableExtra("event") as? Event

        if (event == null) {
            finish()
            return
        }

        // Bind data
        ivDetailBanner.setImageResource(event.imageRes)
        tvDetailTitle.text = event.title
        tvDetailDate.text  = "📅 ${event.date}   ⏰ ${event.time}"
        tvDetailVenue.text = "📍 ${event.venue}   •   ${event.category}"
        tvDetailDesc.text  = event.description

        // Go to Seat Booking
        btnRegister.setOnClickListener {
            val intent = Intent(this, SeatBookingActivity::class.java)
            intent.putExtra("event", event)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
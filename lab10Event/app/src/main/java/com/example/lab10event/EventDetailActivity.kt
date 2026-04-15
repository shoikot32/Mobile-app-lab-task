package com.example.lab10event

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class EventDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        // ✅ SAFE CAST
        val event = intent.getSerializableExtra("event") as? Event

        if (event == null) {
            Toast.makeText(this, "Event data not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // UI binding
        findViewById<ImageView>(R.id.ivDetailBanner).setImageResource(event.imageRes)
        findViewById<TextView>(R.id.tvDetailTitle).text  = event.title
        findViewById<TextView>(R.id.tvDetailDate).text   = "📅 ${event.date}  ⏰ ${event.time}"
        findViewById<TextView>(R.id.tvDetailVenue).text  = "📍 ${event.venue}  •  ${event.category}"
        findViewById<TextView>(R.id.tvDetailDesc).text   = event.description

        // Button action
        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val intent = Intent(this, SeatBookingActivity::class.java)
            intent.putExtra("event", event)
            startActivity(intent)
        }
    }
}
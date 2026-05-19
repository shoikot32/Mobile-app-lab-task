package com.example.skyway

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BookingSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        findViewById<TextView>(R.id.tvSuccess).text = "Booking Successful ✅"
    }
}

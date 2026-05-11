package com.example.lab10event

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SeatBookingActivity : AppCompatActivity() {

    private lateinit var seatGrid: GridView
    private lateinit var tvSummary: TextView
    private lateinit var btnConfirm: Button

    private lateinit var event: Event

    // 0 = available, 1 = booked, 2 = selected
    private val seatStates = IntArray(48) {
        if (Math.random() < 0.3) 1 else 0   // ~30% pre-booked randomly
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_booking)

        seatGrid   = findViewById(R.id.seatGrid)
        tvSummary  = findViewById(R.id.tvSummary)
        btnConfirm = findViewById(R.id.btnConfirm)

        // Get event from intent
        val receivedEvent = intent.getSerializableExtra("event") as? Event
        if (receivedEvent == null) {
            finish()
            return
        }
        event = receivedEvent

        val seatAdapter = object : BaseAdapter() {

            override fun getCount(): Int = 48
            override fun getItem(position: Int): Any = position
            override fun getItemId(position: Int): Long = position.toLong()

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val tv = (convertView as? TextView) ?: TextView(this@SeatBookingActivity).apply {
                    layoutParams = AbsListView.LayoutParams(
                        AbsListView.LayoutParams.MATCH_PARENT,
                        80
                    )
                    gravity = android.view.Gravity.CENTER
                    textSize = 11f
                    setTextColor(Color.WHITE)
                }

                tv.text = "${position + 1}"

                when (seatStates[position]) {
                    1    -> tv.setBackgroundColor(Color.parseColor("#F44336")) // Booked = Red
                    2    -> tv.setBackgroundColor(Color.parseColor("#2196F3")) // Selected = Blue
                    else -> tv.setBackgroundColor(Color.parseColor("#4CAF50")) // Available = Green
                }

                return tv
            }
        }

        seatGrid.adapter = seatAdapter

        // Seat tap
        seatGrid.setOnItemClickListener { _, _, position, _ ->
            when (seatStates[position]) {
                1 -> {
                    // Already booked — cannot select
                    Toast.makeText(this, "Seat ${position + 1} is already booked!", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    // Deselect
                    seatStates[position] = 0
                    seatAdapter.notifyDataSetChanged()
                    updateSummary()
                }
                else -> {
                    // Select
                    seatStates[position] = 2
                    seatAdapter.notifyDataSetChanged()
                    updateSummary()
                }
            }
        }

        // Confirm booking
        btnConfirm.setOnClickListener {
            val selectedCount = seatStates.count { it == 2 }

            if (selectedCount == 0) {
                Toast.makeText(this, "Please select at least one seat!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val totalPrice = selectedCount * event.price

            AlertDialog.Builder(this)
                .setTitle("Confirm Booking")
                .setMessage(
                    "Event: ${event.title}\n" +
                            "Seats: $selectedCount\n" +
                            "Total: ${if (totalPrice == 0.0) "FREE" else "৳${"%.0f".format(totalPrice)}"}\n\n" +
                            "Confirm your booking?"
                )
                .setPositiveButton("Confirm") { _, _ ->
                    Toast.makeText(this, "✅ Booking Confirmed for ${event.title}!", Toast.LENGTH_LONG).show()
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    // Update bottom summary bar
    private fun updateSummary() {
        val selectedCount = seatStates.count { it == 2 }
        val totalPrice    = selectedCount * event.price
        tvSummary.text    =
            "$selectedCount seat(s) selected   •   " +
                    "Total: ${if (totalPrice == 0.0) "FREE" else "৳${"%.0f".format(totalPrice)}"}"
    }

    // Warn user if they try to leave with selected seats
    override fun onBackPressed() {
        val selectedCount = seatStates.count { it == 2 }

        if (selectedCount > 0) {
            AlertDialog.Builder(this)
                .setTitle("Leave Booking?")
                .setMessage("You have $selectedCount seat(s) selected. If you leave now your selection will be lost.")
                .setPositiveButton("Leave") { _, _ ->
                    super.onBackPressed()
                }
                .setNegativeButton("Stay", null)
                .show()
        } else {
            super.onBackPressed()
        }
    }
}
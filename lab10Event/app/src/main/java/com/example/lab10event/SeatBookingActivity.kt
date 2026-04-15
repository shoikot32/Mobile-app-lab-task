package com.example.lab10event

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SeatBookingActivity : AppCompatActivity() {

    // 0=available, 1=booked, 2=selected
    private val seatStates = IntArray(48) { if (Math.random() < 0.3) 1 else 0 }

    private lateinit var event: Event
    private lateinit var tvSummary: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_booking)

        // ✅ SAFE intent handling
        val e = intent.getSerializableExtra("event") as? Event
        if (e == null) {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        event = e

        val seatGrid   = findViewById<GridView>(R.id.seatGrid)
        tvSummary      = findViewById(R.id.tvSummary)
        val btnConfirm = findViewById<Button>(R.id.btnConfirm)

        // ✅ Adapter
        val adapter = object : BaseAdapter() {
            override fun getCount() = seatStates.size
            override fun getItem(pos: Int) = pos
            override fun getItemId(pos: Int) = pos.toLong()

            override fun getView(pos: Int, convertView: View?, parent: ViewGroup): View {
                val tv = (convertView as? TextView) ?: TextView(this@SeatBookingActivity).apply {
                    layoutParams = AbsListView.LayoutParams(
                        AbsListView.LayoutParams.MATCH_PARENT, 100
                    )
                    gravity = Gravity.CENTER
                    textSize = 12f
                    setTextColor(Color.WHITE)
                }

                tv.text = "${pos + 1}"

                val color = when (seatStates[pos]) {
                    1 -> "#F44336" // booked
                    2 -> "#2196F3" // selected
                    else -> "#4CAF50" // available
                }

                tv.setBackgroundColor(Color.parseColor(color))
                return tv
            }
        }

        seatGrid.adapter = adapter

        // ✅ Initial summary
        updateSummary()

        // ✅ Seat click
        seatGrid.setOnItemClickListener { _, _, pos, _ ->
            if (seatStates[pos] == 1) {
                Toast.makeText(this, "Seat already booked!", Toast.LENGTH_SHORT).show()
                return@setOnItemClickListener
            }

            seatStates[pos] = if (seatStates[pos] == 2) 0 else 2

            adapter.notifyDataSetChanged()
            updateSummary()
        }

        // ✅ Confirm booking
        btnConfirm.setOnClickListener {
            val selected = seatStates.count { it == 2 }

            if (selected == 0) {
                Toast.makeText(this, "Select at least one seat", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val total = selected * event.price

            AlertDialog.Builder(this)
                .setTitle("Confirm Booking")
                .setMessage(
                    "Confirm booking of $selected seats for ${event.title}?\nTotal: ৳${"%.0f".format(total)}"
                )
                .setPositiveButton("Confirm") { _, _ ->
                    Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_LONG).show()
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    // ✅ Summary update
    private fun updateSummary() {
        val selected = seatStates.count { it == 2 }
        val total = selected * event.price
        tvSummary.text = "$selected seats selected • Total: ৳${"%.0f".format(total)}"
    }

    // ✅ Modern back handling
    override fun onBackPressed() {
        val selected = seatStates.count { it == 2 }

        if (selected > 0) {
            AlertDialog.Builder(this)
                .setTitle("Leave?")
                .setMessage("You selected $selected seat(s). Leave without booking?")
                .setPositiveButton("Leave") { _, _ -> super.onBackPressed() }
                .setNegativeButton("Stay", null)
                .show()
        } else {
            super.onBackPressed()
        }
    }
}
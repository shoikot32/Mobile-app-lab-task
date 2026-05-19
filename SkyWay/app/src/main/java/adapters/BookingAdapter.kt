package com.example.skyway.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skyway.R
import com.example.skyway.models.Booking
import java.text.SimpleDateFormat
import java.util.*

class BookingAdapter(private val bookingList: List<Booking>) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPackageTitle: TextView = view.findViewById(R.id.tvPackageTitle)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvBookingId: TextView = view.findViewById(R.id.tvBookingId)
        val tvPersons: TextView = view.findViewById(R.id.tvPersons)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookingList[position]

        holder.tvPackageTitle.text = booking.packageTitle
        holder.tvStatus.text = booking.status
        holder.tvBookingId.text = "ID: #${booking.bookingId?.takeLast(6)}"
        holder.tvPersons.text = "${booking.personCount} Persons"
        holder.tvAmount.text = "৳ ${booking.totalAmount}"

        // Format timestamp
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        holder.tvDate.text = sdf.format(Date(booking.timestamp))

        // Status color coding
        if (booking.status == "Confirmed") {
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32"))
            holder.tvStatus.setBackgroundColor(Color.parseColor("#E8F5E9"))
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#C62828"))
            holder.tvStatus.setBackgroundColor(Color.parseColor("#FFEBEE"))
        }
    }

    override fun getItemCount(): Int = bookingList.size
}

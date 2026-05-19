package com.example.skyway.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skyway.R
import com.example.skyway.adapters.BookingAdapter
import com.example.skyway.models.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookingFragment : Fragment(R.layout.fragment_booking) {

    private lateinit var rvBookings: RecyclerView
    private lateinit var pbBookings: ProgressBar
    private lateinit var tvNoBookings: TextView
    private val bookingList = mutableListOf<Booking>()
    private lateinit var adapter: BookingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvBookings = view.findViewById(R.id.rvBookings)
        pbBookings = view.findViewById(R.id.pbBookings)
        tvNoBookings = view.findViewById(R.id.tvNoBookings)

        rvBookings.layoutManager = LinearLayoutManager(requireContext())
        adapter = BookingAdapter(bookingList)
        rvBookings.adapter = adapter

        fetchUserBookings()
    }

    private fun fetchUserBookings() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("bookings")

        pbBookings.visibility = View.VISIBLE

        database.orderByChild("userId").equalTo(currentUserId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    bookingList.clear()
                    for (bookingSnapshot in snapshot.children) {
                        val booking = bookingSnapshot.getValue(Booking::class.java)
                        if (booking != null) {
                            bookingList.add(booking)
                        }
                    }
                    
                    bookingList.sortByDescending { it.timestamp }
                    
                    pbBookings.visibility = View.GONE
                    if (bookingList.isEmpty()) {
                        tvNoBookings.visibility = View.VISIBLE
                    } else {
                        tvNoBookings.visibility = View.GONE
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    pbBookings.visibility = View.GONE
                }
            })
    }
}

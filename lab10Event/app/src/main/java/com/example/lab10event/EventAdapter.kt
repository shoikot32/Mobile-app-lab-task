package com.example.lab10event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(
    private var events: MutableList<Event>,
    private val onClick: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivEventBanner: ImageView = view.findViewById(R.id.ivEventBanner)
        val tvEventTitle: TextView   = view.findViewById(R.id.tvEventTitle)
        val tvEventDate: TextView    = view.findViewById(R.id.tvEventDate)
        val tvEventVenue: TextView   = view.findViewById(R.id.tvEventVenue)
        val tvSeats: TextView        = view.findViewById(R.id.tvSeats)
        val tvPrice: TextView        = view.findViewById(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        holder.ivEventBanner.setImageResource(event.imageRes)
        holder.tvEventTitle.text = event.title
        holder.tvEventDate.text  = "📅 ${event.date}  ⏰ ${event.time}"
        holder.tvEventVenue.text = "📍 ${event.venue}"
        holder.tvSeats.text      = "💺 ${event.availableSeats} seats left"
        holder.tvPrice.text      = if (event.price == 0.0) "FREE"
        else "৳${"%.0f".format(event.price)}"

        holder.itemView.setOnClickListener {
            onClick(event)
        }
    }

    override fun getItemCount(): Int = events.size

    fun updateList(newList: MutableList<Event>) {
        events = newList
        notifyDataSetChanged()
    }
}
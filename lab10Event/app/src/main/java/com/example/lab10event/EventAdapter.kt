package com.example.lab10event

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(
    private var events: MutableList<Event>,
    private val onClick: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivBanner: ImageView = view.findViewById(R.id.ivEventBanner)
        val tvTitle: TextView   = view.findViewById(R.id.tvEventTitle)
        val tvDate: TextView    = view.findViewById(R.id.tvEventDate)
        val tvVenue: TextView   = view.findViewById(R.id.tvEventVenue)
        val tvSeats: TextView   = view.findViewById(R.id.tvSeats)
        val tvPrice: TextView   = view.findViewById(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val e = events[position]

        holder.ivBanner.setImageResource(e.imageRes)
        holder.tvTitle.text = e.title
        holder.tvDate.text  = "📅 ${e.date} • ${e.time}"
        holder.tvVenue.text = "📍 ${e.venue}"
        holder.tvSeats.text = "💺 ${e.availableSeats} seats left"

        holder.tvPrice.text =
            if (e.price == 0.0) "FREE"
            else "৳${"%.0f".format(e.price)}"

        // ✅ Safe click (uses current adapter position)
        holder.itemView.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onClick(events[pos])
            }
        }
    }

    override fun getItemCount(): Int = events.size

    // ✅ Cleaner update (no reference bugs)
    fun updateList(newList: MutableList<Event>) {
        events.clear()
        events.addAll(newList)
        notifyDataSetChanged()
    }
}
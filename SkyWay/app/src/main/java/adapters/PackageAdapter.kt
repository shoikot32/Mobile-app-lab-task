package com.example.skyway.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skyway.R
import com.example.skyway.models.TravelPackage

class PackageAdapter(
    private var list: List<TravelPackage>,
    private val onBookClick: (TravelPackage) -> Unit
) : RecyclerView.Adapter<PackageAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgPackage)
        val title: TextView = view.findViewById(R.id.tvTitle)
        val info: TextView = view.findViewById(R.id.tvInfo)
        val btn: Button = view.findViewById(R.id.btnBook)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_package, parent, false)
        return VH(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]

        // Load image from URL using Glide
        Glide.with(holder.itemView.context)
            .load(item.image)
            .placeholder(R.drawable.skyway_logo)
            .error(R.drawable.skyway_logo)
            .into(holder.img)

        holder.title.text = item.title
        holder.info.text = "${item.location} | ৳${item.price} | ${item.days} days"

        holder.btn.setOnClickListener {
            onBookClick(item)
        }
    }

    fun updateList(newList: List<TravelPackage>) {
        list = newList
        notifyDataSetChanged()
    }
}
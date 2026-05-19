package com.university.newsapp.ui

import android.graphics.Color
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.university.newsapp.R
import com.university.newsapp.model.User

class UserAdapter(
    private val onClick: (User) -> Unit
) : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    private val avatarColors = listOf(
        "#E53935", "#8E24AA", "#1E88E5",
        "#00897B", "#F4511E", "#3949AB"
    )

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAvatar: TextView   = view.findViewById(R.id.tvAvatar)
        val tvName: TextView     = view.findViewById(R.id.tvName)
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val tvEmail: TextView    = view.findViewById(R.id.tvEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)

        val initials = user.name.split(" ")
            .take(2)
            .joinToString("") { it.first().uppercaseChar().toString() }

        holder.tvAvatar.text = initials

        val color = avatarColors[position % avatarColors.size]
        (holder.tvAvatar.background as? android.graphics.drawable.GradientDrawable)
            ?.setColor(Color.parseColor(color))

        holder.tvName.text     = user.name
        holder.tvUsername.text = "@${user.username}"
        holder.tvEmail.text    = user.email

        holder.itemView.setOnClickListener { onClick(user) }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(old: User, new: User) = old.id == new.id
    override fun areContentsTheSame(old: User, new: User) = old == new
}
package com.university.newsapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.university.newsapp.R
import com.university.newsapp.model.Post

class PostAdapter(
    private val onClick: (Post) -> Unit
) : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPostId: TextView = view.findViewById(R.id.tvPostId)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvBody: TextView = view.findViewById(R.id.tvBody)
        val tvUserId: TextView = view.findViewById(R.id.tvUserId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.tvPostId.text = "#${post.id}"
        holder.tvTitle.text = post.title
        holder.tvBody.text = post.body
        holder.tvUserId.text = "User: ${post.userId}"
        
        holder.itemView.setOnClickListener { onClick(post) }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(old: Post, new: Post) = old.id == new.id
    override fun areContentsTheSame(old: Post, new: Post) = old == new
}

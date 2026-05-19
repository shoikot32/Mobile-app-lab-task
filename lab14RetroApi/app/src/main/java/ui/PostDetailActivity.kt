package com.university.newsapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.university.newsapp.R
import com.university.newsapp.model.Comment
import com.university.newsapp.network.RetrofitClient
import kotlinx.coroutines.launch

class PostDetailActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvBody: TextView
    private lateinit var tvAuthorName: TextView
    private lateinit var tvAuthorEmail: TextView
    private lateinit var tvCompany: TextView
    private lateinit var authorCard: View
    private lateinit var commentsContainer: LinearLayout
    private lateinit var progressPost: ProgressBar
    private lateinit var progressComments: ProgressBar

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Post Detail"

        tvTitle = findViewById(R.id.tvTitle)
        tvBody = findViewById(R.id.tvBody)
        tvAuthorName = findViewById(R.id.tvAuthorName)
        tvAuthorEmail = findViewById(R.id.tvAuthorEmail)
        tvCompany = findViewById(R.id.tvCompany)
        authorCard = findViewById(R.id.authorCard)
        commentsContainer = findViewById(R.id.commentsContainer)
        progressPost = findViewById(R.id.progressPost)
        progressComments = findViewById(R.id.progressComments)

        val postId = intent.getIntExtra("postId", -1)
        userId = intent.getIntExtra("userId", -1)

        if (postId != -1) {
            loadPostDetails(postId)
            loadComments(postId)
        }
        
        if (userId != -1) {
            loadAuthorDetails(userId)
        }

        authorCard.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
    }

    private fun loadPostDetails(postId: Int) {
        progressPost.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val post = RetrofitClient.instance.getPostById(postId)
                tvTitle.text = post.title
                tvBody.text = post.body
                progressPost.visibility = View.GONE
            } catch (e: Exception) {
                progressPost.visibility = View.GONE
                Toast.makeText(this@PostDetailActivity, "Error loading post", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadAuthorDetails(userId: Int) {
        lifecycleScope.launch {
            try {
                val user = RetrofitClient.instance.getUserById(userId)
                tvAuthorName.text = user.name
                tvAuthorEmail.text = user.email
                tvCompany.text = user.company.name
            } catch (e: Exception) {
            }
        }
    }

    private fun loadComments(postId: Int) {
        progressComments.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val comments = RetrofitClient.instance.getCommentsByPost(postId)
                progressComments.visibility = View.GONE
                comments.forEach { comment ->
                    val view = layoutInflater.inflate(android.R.layout.simple_list_item_2, commentsContainer, false)
                    view.findViewById<TextView>(android.R.id.text1).text = comment.name
                    view.findViewById<TextView>(android.R.id.text2).text = comment.body
                    commentsContainer.addView(view)
                }
            } catch (e: Exception) {
                progressComments.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

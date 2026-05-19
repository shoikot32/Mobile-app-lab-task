package com.university.newsapp.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.university.newsapp.R
import com.university.newsapp.network.RetrofitClient
import kotlinx.coroutines.launch

class UserProfileActivity : AppCompatActivity() {

    private lateinit var tvAvatar: TextView
    private lateinit var tvName: TextView
    private lateinit var tvUsername: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvWebsite: TextView
    private lateinit var tvCompany: TextView
    private lateinit var tvCatchPhrase: TextView
    private lateinit var postsContainer: LinearLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "User Profile"

        tvAvatar = findViewById(R.id.tvAvatar)
        tvName = findViewById(R.id.tvName)
        tvUsername = findViewById(R.id.tvUsername)
        tvEmail = findViewById(R.id.tvEmail)
        tvPhone = findViewById(R.id.tvPhone)
        tvWebsite = findViewById(R.id.tvWebsite)
        tvCompany = findViewById(R.id.tvCompany)
        tvCatchPhrase = findViewById(R.id.tvCatchPhrase)
        postsContainer = findViewById(R.id.postsContainer)
        progressBar = findViewById(R.id.progressBar)

        val userId = intent.getIntExtra("userId", -1)
        if (userId != -1) {
            loadUserProfile(userId)
            loadUserPosts(userId)
        }
    }

    private fun loadUserProfile(userId: Int) {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val user = RetrofitClient.instance.getUserById(userId)
                
                val initials = user.name.split(" ")
                    .take(2)
                    .joinToString("") { it.first().uppercase() }
                
                tvAvatar.text = initials
                tvName.text = user.name
                tvUsername.text = "@${user.username}"
                tvEmail.text = "✉️ ${user.email}"
                tvPhone.text = "📞 ${user.phone}"
                tvWebsite.text = "🌐 ${user.website}"
                tvCompany.text = "🏢 ${user.company.name}"
                tvCatchPhrase.text = user.company.catchPhrase
                
                progressBar.visibility = View.GONE
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@UserProfileActivity, "Error loading profile", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadUserPosts(userId: Int) {
        lifecycleScope.launch {
            try {
                val posts = RetrofitClient.instance.getPostsByUser(userId)
                posts.forEach { post ->
                    val view = layoutInflater.inflate(android.R.layout.simple_list_item_2, postsContainer, false)
                    view.findViewById<TextView>(android.R.id.text1).text = post.title
                    view.findViewById<TextView>(android.R.id.text2).text = post.body
                    postsContainer.addView(view)
                }
            } catch (e: Exception) {
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

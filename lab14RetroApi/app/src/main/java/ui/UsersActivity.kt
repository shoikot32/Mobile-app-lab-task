package com.university.newsapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.university.newsapp.R
import com.university.newsapp.network.RetrofitClient
import kotlinx.coroutines.launch

class UsersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        supportActionBar?.title = "All Users"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar  = findViewById(R.id.progressBar)

        userAdapter = UserAdapter { user ->
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra("userId", user.id)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter

        loadUsers()
    }

    private fun loadUsers() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val users = RetrofitClient.instance.getAllUsers()
                progressBar.visibility  = View.GONE
                userAdapter.submitList(users)
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@UsersActivity, "Failed to load users", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

package com.university.newsapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.university.newsapp.R
import com.university.newsapp.model.Post
import com.university.newsapp.network.RetrofitClient
import android.view.Menu
import android.view.MenuItem
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var btnRetry: Button
    private lateinit var searchView: SearchView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var postAdapter: PostAdapter
    private var allPosts = listOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar  = findViewById(R.id.progressBar)
        tvError      = findViewById(R.id.tvError)
        btnRetry     = findViewById(R.id.btnRetry)
        searchView   = findViewById(R.id.searchView)
        swipeRefresh = findViewById(R.id.swipeRefresh)

        postAdapter = PostAdapter { post ->
            val intent = Intent(this, PostDetailActivity::class.java)
            intent.putExtra("postId", post.id)
            intent.putExtra("userId", post.userId)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = postAdapter

        swipeRefresh.setOnRefreshListener {
            loadPosts()
        }

        btnRetry.setOnClickListener { loadPosts() }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?) = false
            override fun onQueryTextChange(q: String?): Boolean {
                val filtered = allPosts.filter {
                    it.title.contains(q ?: "", ignoreCase = true)
                }
                postAdapter.submitList(filtered)
                return true
            }
        })

        loadPosts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_users) {
            startActivity(Intent(this, UsersActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadPosts() {
        progressBar.visibility  = View.VISIBLE
        recyclerView.visibility = View.GONE
        tvError.visibility      = View.GONE
        btnRetry.visibility     = View.GONE

        lifecycleScope.launch {
            try {
                val posts = RetrofitClient.instance.getAllPosts()
                allPosts  = posts

                progressBar.visibility  = View.GONE
                recyclerView.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                postAdapter.submitList(posts)

            } catch (e: HttpException) {
                showError("Server error: ${e.code()}")
            } catch (e: IOException) {
                showError("Network error. Check your connection.")
            } catch (e: Exception) {
                showError("Something went wrong: ${e.message}")
            }
        }
    }

    private fun showError(message: String) {
        progressBar.visibility    = View.GONE
        swipeRefresh.isRefreshing = false
        tvError.visibility        = View.VISIBLE
        tvError.text              = message
        btnRetry.visibility       = View.VISIBLE
    }
}
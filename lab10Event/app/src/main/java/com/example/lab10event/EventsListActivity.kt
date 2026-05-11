package com.example.lab10event

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EventsListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var chipContainer: LinearLayout
    private lateinit var adapter: EventAdapter

    private val categories = listOf("All", "Tech", "Sports", "Cultural", "Academic", "Social")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_list)

        recyclerView  = findViewById(R.id.recyclerView)
        searchView    = findViewById(R.id.searchView)
        chipContainer = findViewById(R.id.chipContainer)

        // Setup RecyclerView
        adapter = EventAdapter(SampleData.events.toMutableList()) { event ->
            val intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("event", event)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Category filter buttons
        categories.forEach { category ->
            val btn = Button(this)
            btn.text = category
            btn.setOnClickListener {
                filterByCategory(category)
            }
            chipContainer.addView(btn)
        }

        // Search filter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filtered = SampleData.events.filter { event ->
                    event.title.contains(newText ?: "", ignoreCase = true)
                }.toMutableList()
                adapter.updateList(filtered)
                return true
            }
        })
    }

    private fun filterByCategory(category: String) {
        val filtered = if (category == "All") {
            SampleData.events.toMutableList()
        } else {
            SampleData.events.filter { it.category == category }.toMutableList()
        }
        adapter.updateList(filtered)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
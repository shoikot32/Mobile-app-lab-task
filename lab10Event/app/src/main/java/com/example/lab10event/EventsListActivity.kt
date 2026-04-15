package com.example.lab10event

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EventsListActivity : AppCompatActivity() {

    private val categories = listOf("All","Tech","Sports","Cultural","Academic","Social")

    private lateinit var adapter: EventAdapter
    private var currentList = SampleData.events.toMutableList()   // 🔥 keep current state

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_list)

        val recyclerView  = findViewById<RecyclerView>(R.id.recyclerView)
        val searchView    = findViewById<SearchView>(R.id.searchView)
        val chipContainer = findViewById<LinearLayout>(R.id.chipContainer)

        // Adapter
        adapter = EventAdapter(currentList) { event ->
            val intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("event", event)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // =========================
        // 🔘 CATEGORY FILTER
        // =========================
        categories.forEach { cat ->
            val btn = Button(this).apply {
                text = cat

                setOnClickListener {
                    currentList = if (cat == "All") {
                        SampleData.events.toMutableList()
                    } else {
                        SampleData.events.filter { it.category == cat }.toMutableList()
                    }

                    adapter.updateList(currentList)
                }
            }
            chipContainer.addView(btn)
        }

        // =========================
        // 🔍 SEARCH FILTER
        // =========================
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(q: String?) = false

            override fun onQueryTextChange(q: String?): Boolean {
                val query = q?.trim() ?: ""

                val filtered = currentList.filter {
                    it.title.contains(query, ignoreCase = true)
                }.toMutableList()

                adapter.updateList(filtered)
                return true
            }
        })
    }
}
package com.example.lab7photo

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val categories = listOf("All", "Nature", "City", "Animals", "Food", "Travel")
    private var selectionMode = false

    private lateinit var adapter: PhotoAdapter
    private lateinit var selectionToolbar: LinearLayout
    private lateinit var tvSelectedCount: TextView

    private val allPhotos = mutableListOf(
        Photo(1, R.mipmap.ic_launcher, "Forest", "Nature"),
        Photo(2, R.mipmap.ic_launcher, "Mountain", "Nature"),
        Photo(3, R.mipmap.ic_launcher, "City", "City"),
        Photo(4, R.mipmap.ic_launcher, "Bridge", "City"),
        Photo(5, R.mipmap.ic_launcher, "Lion", "Animals"),
        Photo(6, R.mipmap.ic_launcher, "Elephant", "Animals"),
        Photo(7, R.mipmap.ic_launcher, "Pizza", "Food"),
        Photo(8, R.mipmap.ic_launcher, "Sushi", "Food"),
        Photo(9, R.mipmap.ic_launcher, "Beach", "Travel"),
        Photo(10, R.mipmap.ic_launcher, "Paris", "Travel")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectionToolbar = findViewById(R.id.selectionToolbar)
        tvSelectedCount = findViewById(R.id.tvSelectedCount)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        val gridView = findViewById<GridView>(R.id.gridView)
        val tabContainer = findViewById<LinearLayout>(R.id.tabContainer)

        adapter = PhotoAdapter(this, allPhotos.toMutableList())
        gridView.adapter = adapter

        // Category tabs
        categories.forEach { cat ->
            val btn = Button(this)
            btn.text = cat
            btn.setOnClickListener {
                val filtered = if (cat == "All") allPhotos
                else allPhotos.filter { it.category == cat }
                adapter.updatePhotos(filtered.toMutableList())
            }
            tabContainer.addView(btn)
        }

        // Click
        gridView.setOnItemClickListener { _, _, pos, _ ->
            if (selectionMode) {
                val photo = adapter.getPhotos()[pos]
                photo.isSelected = !photo.isSelected

                val count = adapter.getPhotos().count { it.isSelected }
                tvSelectedCount.text = "$count selected"

                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "Long press to select", Toast.LENGTH_SHORT).show()
            }
        }

        // Long press → start selection
        gridView.setOnItemLongClickListener { _, _, pos, _ ->
            if (!selectionMode) {
                selectionMode = true
                adapter.setSelectionMode(true)
                selectionToolbar.visibility = View.VISIBLE
            }

            adapter.getPhotos()[pos].isSelected = true
            tvSelectedCount.text = "1 selected"

            adapter.notifyDataSetChanged()
            true
        }

        // Delete
        btnDelete.setOnClickListener {
            val remaining = adapter.getPhotos().filter { !it.isSelected }
            val deletedCount = adapter.getPhotos().size - remaining.size

            adapter.updatePhotos(remaining.toMutableList())

            selectionMode = false
            adapter.setSelectionMode(false)
            selectionToolbar.visibility = View.GONE

            Toast.makeText(this, "$deletedCount deleted", Toast.LENGTH_SHORT).show()
        }
    }
}
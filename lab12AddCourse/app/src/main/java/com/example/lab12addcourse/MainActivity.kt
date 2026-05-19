package com.example.lab12addcourse

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var searchView: SearchView
    private lateinit var adapter: CourseAdapter

    private val dbRef = FirebaseDatabase.getInstance().getReference("courses")

    private val allCourses = mutableListOf<Course>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        tvEmpty      = findViewById(R.id.tvEmpty)
        searchView   = findViewById(R.id.searchView)

        adapter = CourseAdapter(
            mutableListOf(),
            onEdit   = { course -> openEditActivity(course) },
            onDelete = { course -> confirmDelete(course) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(
            R.id.fab
        ).setOnClickListener {
            startActivity(Intent(this, AddCourseActivity::class.java))
        }

        loadCoursesFromFirebase()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?) = false
            override fun onQueryTextChange(q: String?): Boolean {
                filterCourses(q ?: "")
                return true
            }
        })
    }

    private fun loadCoursesFromFirebase() {
        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                allCourses.clear()

                for (child in snapshot.children) {
                    val course = child.getValue(Course::class.java)
                    if (course != null) {
                        course.id = child.key ?: ""
                        allCourses.add(course)
                    }
                }

                adapter.updateList(allCourses.toMutableList())
                updateEmptyState()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@MainActivity,
                    "Error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun filterCourses(query: String) {
        val filtered = allCourses.filter {
            it.courseName.contains(query, ignoreCase = true) ||
            it.courseCode.contains(query, ignoreCase = true)
        }.toMutableList()
        adapter.updateList(filtered)
    }

    private fun updateEmptyState() {
        if (allCourses.isEmpty()) {
            tvEmpty.visibility       = View.VISIBLE
            recyclerView.visibility  = View.GONE
        } else {
            tvEmpty.visibility       = View.GONE
            recyclerView.visibility  = View.VISIBLE
        }
    }

    private fun openEditActivity(course: Course) {
        val intent = Intent(this, EditCourseActivity::class.java)
        intent.putExtra("course", course)
        startActivity(intent)
    }

    private fun confirmDelete(course: Course) {
        AlertDialog.Builder(this)
            .setTitle("Delete Course")
            .setMessage("Delete ${course.courseName}?")
            .setPositiveButton("Delete") { _, _ ->
                dbRef.child(course.id).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}


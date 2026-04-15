package com.example.lab5contact

import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    private lateinit var tvEmpty: TextView
    private lateinit var fab: com.google.android.material.floatingactionbutton.FloatingActionButton

    // 🔴 Data model (name + number)
    data class Contact(val name: String, val phone: String)

    private val contactList = mutableListOf<Contact>()
    private val filteredList = mutableListOf<Contact>()

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView = findViewById(R.id.searchView)
        listView = findViewById(R.id.listView)
        tvEmpty = findViewById(R.id.tvEmpty)
        fab = findViewById(R.id.fab)

        // Sample data
        contactList.addAll(
            listOf(
                Contact("SHARIEAR", "01711111111"),
                Contact("SHOIKOT", "01822222222")
            )
        )
        filteredList.addAll(contactList)

        setupAdapter()
        updateEmptyView()

        // 🔍 Search
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        // ➕ Add contact
        fab.setOnClickListener {
            showAddDialog()
        }

        // Click item
        listView.setOnItemClickListener { _, _, position, _ ->
            val c = filteredList[position]
            Toast.makeText(this, "${c.name} - ${c.phone}", Toast.LENGTH_SHORT).show()
        }
    }

    // 🔧 Adapter shows name + number
    private fun setupAdapter() {
        val displayList = filteredList.map { "${it.name}\n${it.phone}" }

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            displayList
        )

        listView.adapter = adapter
    }

    // 🔍 Search both name & number
    private fun filterList(query: String?) {
        filteredList.clear()

        if (query.isNullOrEmpty()) {
            filteredList.addAll(contactList)
        } else {
            val q = query.lowercase()
            for (c in contactList) {
                if (c.name.lowercase().contains(q) || c.phone.contains(q)) {
                    filteredList.add(c)
                }
            }
        }

        setupAdapter()
        updateEmptyView()
    }

    // ➕ Add dialog (name + number)
    private fun showAddDialog() {

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40, 20, 40, 10)

        val nameInput = EditText(this)
        nameInput.hint = "Name"

        val phoneInput = EditText(this)
        phoneInput.hint = "Phone Number"
        phoneInput.inputType = InputType.TYPE_CLASS_PHONE

        layout.addView(nameInput)
        layout.addView(phoneInput)

        AlertDialog.Builder(this)
            .setTitle("Add Contact")
            .setView(layout)
            .setPositiveButton("Add") { _, _ ->

                val name = nameInput.text.toString().trim()
                val phone = phoneInput.text.toString().trim()

                if (name.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(this, "Enter valid data", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                contactList.add(Contact(name, phone))
                filterList(searchView.query.toString())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateEmptyView() {
        if (filteredList.isEmpty()) {
            tvEmpty.visibility = TextView.VISIBLE
            listView.visibility = ListView.GONE
        } else {
            tvEmpty.visibility = TextView.GONE
            listView.visibility = ListView.VISIBLE
        }
    }
}
package com.example.skyway.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skyway.PackageDetailsActivity
import com.example.skyway.R
import com.example.skyway.adapters.PackageAdapter
import com.example.skyway.utils.DummyData
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ExploreFragment : Fragment(R.layout.fragment_explore) {

    private lateinit var adapter: PackageAdapter
    private var currentType: String = "All"
    private var currentSearchQuery: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvExplore = view.findViewById<RecyclerView>(R.id.rvExplore)
        val etSearch = view.findViewById<EditText>(R.id.etSearch)
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupCategories)

        // Show a vertical list for Explore
        rvExplore.layoutManager = LinearLayoutManager(requireContext())

        // Start with all packages
        adapter = PackageAdapter(DummyData.packageList.toList()) { pkg ->
            val intent = Intent(requireContext(), PackageDetailsActivity::class.java)
            intent.putExtra("title", pkg.title)
            intent.putExtra("location", pkg.location)
            intent.putExtra("price", pkg.price)
            intent.putExtra("days", pkg.days)
            intent.putExtra("description", pkg.description)
            intent.putExtra("image", pkg.image)
            intent.putExtra("videoId", pkg.videoId)
            startActivity(intent)
        }
        rvExplore.adapter = adapter

        // Search logic
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentSearchQuery = s.toString().lowercase()
                filterData()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Category filter logic
        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val chip = group.findViewById<Chip>(checkedIds[0])
                currentType = chip.text.toString()
                filterData()
            }
        }
    }

    private fun filterData() {
        val result = DummyData.packageList.filter { pkg ->
            val matchesSearch = pkg.title.lowercase().contains(currentSearchQuery) ||
                                pkg.location.lowercase().contains(currentSearchQuery) ||
                                pkg.type.lowercase().contains(currentSearchQuery)
            
            val matchesType = currentType == "All" || pkg.type == currentType
            
            matchesSearch && matchesType
        }
        adapter.updateList(result)
    }
}

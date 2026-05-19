package com.example.skyway.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skyway.PackageDetailsActivity
import com.example.skyway.R
import com.example.skyway.adapters.PackageAdapter
import com.example.skyway.utils.DummyData

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        // Set GridLayoutManager with 2 columns
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        recyclerView.adapter = PackageAdapter(DummyData.packageList) { pkg ->
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
    }
}

package com.example.skyway.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.skyway.LoginActivity
import com.example.skyway.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        val tvUserEmail = view.findViewById<TextView>(R.id.tvUserEmail)
        val btnLogout = view.findViewById<MaterialButton>(R.id.btnLogout)
        val btnMyBookings = view.findViewById<TextView>(R.id.btnMyBookings)

        // Set basic info from Auth
        tvUserEmail.text = user?.email ?: "Guest"
        
        // Fetch user name from database (stored under 'Users')
        if (user != null) {
            val database = FirebaseDatabase.getInstance("https://skyway-c4a96-default-rtdb.firebaseio.com/")
                .getReference("Users").child(user.uid)
            database.child("name").get().addOnSuccessListener {
                if (it.exists()) {
                    tvUserName.text = it.value.toString()
                } else {
                    tvUserName.text = user.displayName ?: "User Name"
                }
            }
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btnMyBookings.setOnClickListener {
            // Navigate to Booking tab by calling activity method or using parent manager
            // For now, assume user will just click the bottom nav
        }
    }
}

package com.example.skyway

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.skyway.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        replaceFragment(HomeFragment())

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_explore -> replaceFragment(ExploreFragment())
                R.id.nav_booking -> replaceFragment(BookingFragment())
                R.id.nav_profile -> replaceFragment(ProfileFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}

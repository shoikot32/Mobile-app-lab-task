package com.example.lab12firebaseauth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * SplashActivity
 * - Shows splash screen on app launch
 * - Waits for 2 seconds
 * - Checks if user is logged in or not
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Delay for 2 seconds, then check authentication state
        Handler(Looper.getMainLooper()).postDelayed({
            checkAuthState()
        }, SPLASH_DELAY)
    }

    /**
     * Checks whether the user is logged in
     */
    private fun checkAuthState() {
        val nextActivity = if (auth.currentUser != null) {
            HomeActivity::class.java
        } else {
            LoginActivity::class.java
        }

        startActivity(Intent(this, nextActivity))
        finish() // Prevent returning to splash screen
    }

    companion object {
        private const val SPLASH_DELAY: Long = 2000 // 2 seconds
    }
}
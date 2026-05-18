package com.example.lab12firebaseauth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

// SplashActivity — App open হলে প্রথমে এই screen দেখাবে
// ২ সেকেন্ড পরে check করবে — user logged in কিনা
class SplashActivity : AppCompatActivity() {

    // FirebaseAuth instance — authentication এর সব কাজ এটি দিয়ে হবে
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // FirebaseAuth instance নেওয়া হচ্ছে
        auth = FirebaseAuth.getInstance()

        // ২ সেকেন্ড পরে check করা হবে
        Handler(Looper.getMainLooper()).postDelayed({
            checkAuthState()
        }, 2000) // 2000 millisecond = 2 second
    }

    // checkAuthState — user logged in কিনা check করা
    private fun checkAuthState() {
        // currentUser null হলে মানে কেউ logged in নেই
        if (auth.currentUser != null) {
            // Already logged in — Home Dashboard এ যাবে
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            // Logged in না — Login screen এ যাবে
            startActivity(Intent(this, LoginActivity::class.java))
        }
        // Splash screen বন্ধ করা হচ্ছে — Back চাপলে Splash এ ফিরবে না
        finish()
    }
}
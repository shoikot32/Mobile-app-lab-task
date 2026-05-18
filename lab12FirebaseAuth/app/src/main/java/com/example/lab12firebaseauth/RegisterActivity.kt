package com.example.lab12firebaseauth
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

// RegisterActivity — নতুন Account তৈরি করার Screen
class RegisterActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText      // পুরো নাম
    private lateinit var etEmail: EditText         // Email
    private lateinit var etPassword: EditText      // Password
    private lateinit var etConfirmPass: EditText   // Confirm Password
    private lateinit var btnRegister: Button       // Register button
    private lateinit var tvLoginLink: TextView     // Login এ যাওয়ার link
    private lateinit var progressBar: ProgressBar  // Loading indicator

    // FirebaseAuth — Firebase এর authentication service
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // FirebaseAuth instance নেওয়া হচ্ছে — Singleton
        auth = FirebaseAuth.getInstance()

        etFullName     = findViewById(R.id.etFullName)
        etEmail        = findViewById(R.id.etEmail)
        etPassword     = findViewById(R.id.etPassword)
        etConfirmPass  = findViewById(R.id.etConfirmPass)
        btnRegister    = findViewById(R.id.btnRegister)
        tvLoginLink    = findViewById(R.id.tvLoginLink)
        progressBar    = findViewById(R.id.progressBar)

        // Register button click
        btnRegister.setOnClickListener { registerUser() }

        // Login link — LoginActivity তে যাবে
        tvLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    // registerUser — Input validate করে Firebase এ account তৈরি করবে
    private fun registerUser() {
        val name        = etFullName.text.toString().trim()
        val email       = etEmail.text.toString().trim()
        val password    = etPassword.text.toString().trim()
        val confirmPass = etConfirmPass.text.toString().trim()

        // Validation — সব field check করা হচ্ছে
        if (name.isEmpty() || email.isEmpty() ||
            password.isEmpty() || confirmPass.isEmpty()) {
            showSnackbar("Please fill all fields")
            return
        }

        // Patterns.EMAIL_ADDRESS — Android এর built-in email validator
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showSnackbar("Invalid email address")
            return
        }

        // Password কমপক্ষে ৮ character হতে হবে
        if (password.length < 8) {
            showSnackbar("Password must be at least 8 characters")
            return
        }

        // দুটো password মিলতে হবে
        if (password != confirmPass) {
            showSnackbar("Passwords do not match")
            return
        }

        // Loading শুরু করা হচ্ছে
        showLoading(true)

        // Firebase এ account তৈরি করা হচ্ছে
        // createUserWithEmailAndPassword — async operation
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                showLoading(false) // Loading বন্ধ

                if (task.isSuccessful) {
                    // Account তৈরি সফল — Home এ যাবে
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finishAffinity() // সব আগের screen বন্ধ
                } else {
                    // Firebase error message দেখানো হচ্ছে
                    showSnackbar(task.exception?.message ?: "Registration failed")
                }
            }
    }

    // showLoading — ProgressBar দেখানো বা লুকানো
    private fun showLoading(show: Boolean) {
        progressBar.visibility  = if (show) View.VISIBLE else View.GONE
        btnRegister.isEnabled   = !show // Loading এ button disable
    }

    // showSnackbar — নিচে error message দেখানোর helper
    private fun showSnackbar(message: String) {
        Snackbar.make(btnRegister, message, Snackbar.LENGTH_LONG).show()
    }
}
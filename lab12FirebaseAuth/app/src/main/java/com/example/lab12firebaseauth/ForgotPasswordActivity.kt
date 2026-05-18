package com.example.lab12firebaseauth

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

// ForgotPasswordActivity — Password Reset Email পাঠানোর Screen
class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var btnSendReset: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Back button চালু করা
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Forgot Password"

        auth        = FirebaseAuth.getInstance()
        etEmail     = findViewById(R.id.etEmail)
        btnSendReset = findViewById(R.id.btnSendReset)

        btnSendReset.setOnClickListener { sendResetEmail() }
    }

    // sendResetEmail — Firebase দিয়ে Password Reset Email পাঠানো
    private fun sendResetEmail() {
        val email = etEmail.text.toString().trim()

        if (email.isEmpty()) {
            Snackbar.make(btnSendReset, "Please enter your email", Snackbar.LENGTH_SHORT).show()
            return
        }

        // sendPasswordResetEmail — Firebase এই email এ reset link পাঠাবে
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Reset email sent. Check your inbox.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish() // এই screen বন্ধ করে Login এ ফিরে যাবে
                } else {
                    Snackbar.make(
                        btnSendReset,
                        task.exception?.message ?: "Failed to send reset email",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
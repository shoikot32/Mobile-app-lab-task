package com.example.lab12firebaseauth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

// LoginActivity — Existing user এর Login Screen
class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvForgotPass: TextView    // Forgot Password link
    private lateinit var tvRegisterLink: TextView  // Register link
    private lateinit var progressBar: ProgressBar

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        etEmail        = findViewById(R.id.etEmail)
        etPassword     = findViewById(R.id.etPassword)
        btnLogin       = findViewById(R.id.btnLogin)
        tvForgotPass   = findViewById(R.id.tvForgotPass)
        tvRegisterLink = findViewById(R.id.tvRegisterLink)
        progressBar    = findViewById(R.id.progressBar)

        // Login button click
        btnLogin.setOnClickListener { loginUser() }

        // Forgot Password — ForgotPasswordActivity তে যাবে
        tvForgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        // Register link — RegisterActivity তে যাবে
        tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    // loginUser — Email ও Password দিয়ে Firebase Login
    private fun loginUser() {
        val email    = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            showSnackbar("Please enter email and password")
            return
        }

        showLoading(true)

        // Firebase signInWithEmailAndPassword — login করানো হচ্ছে
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                showLoading(false)

                if (task.isSuccessful) {
                    // Login সফল — Home এ যাবে
                    startActivity(Intent(this, HomeActivity::class.java))
                    finishAffinity() // Login screen বন্ধ — Back চাপলে আর আসবে না
                } else {
                    // Firebase error — ভুল password বা user not found
                    showSnackbar(task.exception?.message ?: "Login failed")
                }
            }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btnLogin.isEnabled     = !show
    }

    private fun showSnackbar(msg: String) {
        Snackbar.make(btnLogin, msg, Snackbar.LENGTH_LONG).show()
    }
}
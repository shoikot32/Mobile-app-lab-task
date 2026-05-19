package com.example.skyway

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvForgotPass: TextView
    private lateinit var tvRegisterLink: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // View Binding
        etEmail        = findViewById(R.id.etEmail)
        etPassword     = findViewById(R.id.etPassword)
        btnLogin       = findViewById(R.id.btnLogin)
        tvForgotPass   = findViewById(R.id.tvForgotPass)
        tvRegisterLink = findViewById(R.id.tvRegisterLink)
        progressBar    = findViewById(R.id.progressBar)

        // Login Button
        btnLogin.setOnClickListener {
            loginUser()
        }

        // Forgot Password
        tvForgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        // Register Page
        tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validation
        if (email.isEmpty() || password.isEmpty()) {
            showSnackbar("Please enter email and password")
            return
        }

        showLoading(true)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                showLoading(false)

                if (task.isSuccessful) {

                    // ✅ IMPORTANT FIX: Go to MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()

                } else {
                    showSnackbar("Invalid email or password")
                }
            }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btnLogin.isEnabled = !show
    }

    private fun showSnackbar(msg: String) {
        Snackbar.make(btnLogin, msg, Snackbar.LENGTH_LONG).show()
    }
}

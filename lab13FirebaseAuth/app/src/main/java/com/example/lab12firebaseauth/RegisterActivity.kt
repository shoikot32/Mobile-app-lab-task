package com.example.lab12firebaseauth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPass: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLoginLink: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        etFullName     = findViewById(R.id.etFullName)
        etEmail        = findViewById(R.id.etEmail)
        etPassword     = findViewById(R.id.etPassword)
        etConfirmPass  = findViewById(R.id.etConfirmPass)
        btnRegister    = findViewById(R.id.btnRegister)
        tvLoginLink    = findViewById(R.id.tvLoginLink)
        progressBar    = findViewById(R.id.progressBar)

        btnRegister.setOnClickListener { registerUser() }

        tvLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser() {
        val name        = etFullName.text.toString().trim()
        val email       = etEmail.text.toString().trim()
        val password    = etPassword.text.toString().trim()
        val confirmPass = etConfirmPass.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
            showSnackbar("Please fill all fields")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showSnackbar("Invalid email address")
            return
        }

        if (password.length < 8) {
            showSnackbar("Password must be at least 8 characters")
            return
        }

        if (password != confirmPass) {
            showSnackbar("Passwords do not match")
            return
        }

        showLoading(true)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                showLoading(false)

                if (task.isSuccessful) {

                    val userId = auth.currentUser!!.uid
                    val database = FirebaseDatabase.getInstance().getReference("Users")

                    val userMap = HashMap<String, String>()
                    userMap["name"] = name
                    userMap["email"] = email

                    database.child(userId).setValue(userMap)
                        .addOnCompleteListener {

                            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()

                            FirebaseAuth.getInstance().signOut()

                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }

                } else {
                    showSnackbar(task.exception?.message ?: "Registration failed")
                }
            }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btnRegister.isEnabled = !show
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(btnRegister, message, Snackbar.LENGTH_LONG).show()
    }
}

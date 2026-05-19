package com.example.skyway

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPass: EditText
    private lateinit var etPhone: EditText

    private lateinit var btnRegister: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        etUsername = findViewById(R.id.etUsername)
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPass = findViewById(R.id.etConfirmPass)
        etPhone = findViewById(R.id.etPhone)

        btnRegister = findViewById(R.id.btnRegister)
        progressBar = findViewById(R.id.progressBar)

        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {

        val username = etUsername.text.toString().trim()
        val name = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPass = etConfirmPass.text.toString().trim()

        // ✅ Validation
        if (username.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPass) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        // ✅ Direct Firebase Registration (NO OTP)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    val userId = auth.currentUser!!.uid

                    val userMap = HashMap<String, String>()
                    userMap["username"] = username
                    userMap["name"] = name
                    userMap["email"] = email
                    userMap["phone"] = phone

                    FirebaseDatabase.getInstance("https://skyway-c4a96-default-rtdb.firebaseio.com/")
                        .getReference("Users")
                        .child(userId)
                        .setValue(userMap)
                        .addOnCompleteListener {

                            progressBar.visibility = View.GONE

                            Toast.makeText(
                                this,
                                "Registration Successful ✅",
                                Toast.LENGTH_SHORT
                            ).show()

                            // ✅ Go to Main directly
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }

                } else {
                    progressBar.visibility = View.GONE

                    Toast.makeText(
                        this,
                        "Error: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
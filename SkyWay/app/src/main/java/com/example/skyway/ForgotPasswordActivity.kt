package com.example.skyway

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var btnSend: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etEmail = findViewById(R.id.etEmail)
        btnSend = findViewById(R.id.btnSendReset)
        auth = FirebaseAuth.getInstance()

        btnSend.setOnClickListener {

            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Reset email sent ✅", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Error sending email", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
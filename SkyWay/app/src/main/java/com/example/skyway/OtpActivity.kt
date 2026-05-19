package com.example.skyway

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.*
import com.google.firebase.FirebaseException
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {

    private lateinit var etOtp: EditText
    private lateinit var btnVerify: Button
    private lateinit var auth: FirebaseAuth

    private var verificationId: String? = null
    private var phone: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        etOtp = findViewById(R.id.etOtp)
        btnVerify = findViewById(R.id.btnVerifyOtp)
        auth = FirebaseAuth.getInstance()

        phone = intent.getStringExtra("phone")

        if (phone != null) {
            sendOtp(phone!!)
        } else {
            Toast.makeText(this, "Phone number missing", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnVerify.setOnClickListener {
            val code = etOtp.text.toString().trim()

            if (code.isEmpty()) {
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
            } else {
                verifyOtp(code)
            }
        }
    }

    private fun sendOtp(phone: String) {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signIn(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@OtpActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(
            verId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            verificationId = verId
            Toast.makeText(this@OtpActivity, "OTP Sent ✅", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyOtp(code: String) {

        if (verificationId == null) {
            Toast.makeText(this, "Verification ID missing", Toast.LENGTH_SHORT).show()
            return
        }

        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signIn(credential)
    }

    private fun signIn(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val name = intent.getStringExtra("name")
                    val email = intent.getStringExtra("email")
                    val password = intent.getStringExtra("password")

                    auth.createUserWithEmailAndPassword(email!!, password!!)
                        .addOnCompleteListener { createTask ->

                            if (createTask.isSuccessful) {

                                val userId = auth.currentUser!!.uid
                                val database = FirebaseDatabase
                                    .getInstance()
                                    .getReference("Users")

                                val userMap = HashMap<String, String>()
                                userMap["name"] = name ?: ""
                                userMap["email"] = email
                                userMap["phone"] = phone ?: ""

                                database.child(userId).setValue(userMap)

                                Toast.makeText(
                                    this,
                                    "Registration Complete ✅",
                                    Toast.LENGTH_SHORT
                                ).show()

                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()

                            } else {
                                Toast.makeText(
                                    this,
                                    "Account creation failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                } else {
                    Toast.makeText(this, "Invalid OTP ❌", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
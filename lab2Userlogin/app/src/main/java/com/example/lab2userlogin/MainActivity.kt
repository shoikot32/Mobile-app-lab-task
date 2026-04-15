package com.example.lab2userlogin

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var ivLogo: ImageView
    lateinit var tvTitle: TextView
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var tvForgotPassword: TextView
    lateinit var btnLogin: Button
    lateinit var progressBar: ProgressBar
    lateinit var profileCard: LinearLayout
    lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivLogo = findViewById(R.id.ivLogo)
        tvTitle = findViewById(R.id.tvTitle)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)
        profileCard = findViewById(R.id.profileCard)
        btnLogout = findViewById(R.id.btnLogout)

        btnLogin.setOnClickListener {
            val user = etUsername.text.toString()
            val pass = etPassword.text.toString()

            progressBar.visibility = View.VISIBLE

            btnLogin.postDelayed({
                progressBar.visibility = View.GONE

                if (user == "admin" && pass == "1234") {
                    profileCard.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this, "Invalid Info", Toast.LENGTH_SHORT).show()
                }
            }, 2000)
        }

        btnLogout.setOnClickListener {
            profileCard.visibility = View.GONE
            etUsername.text.clear()
            etPassword.text.clear()
        }
    }
}
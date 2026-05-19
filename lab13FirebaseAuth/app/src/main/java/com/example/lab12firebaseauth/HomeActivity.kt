package com.example.lab12firebaseauth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var tvEmail: TextView
    private lateinit var tvUid: TextView
    private lateinit var tvCreatedAt: TextView
    private lateinit var tvInitial: TextView
    private lateinit var btnLogout: Button
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmNewPass: EditText
    private lateinit var btnUpdatePass: Button
    private lateinit var btnDeleteAccount: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        tvEmail = findViewById(R.id.tvEmail)
        tvUid = findViewById(R.id.tvUid)
        tvCreatedAt = findViewById(R.id.tvCreatedAt)
        tvInitial = findViewById(R.id.tvInitial)
        btnLogout = findViewById(R.id.btnLogout)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmNewPass = findViewById(R.id.etConfirmNewPass)
        btnUpdatePass = findViewById(R.id.btnUpdatePass)
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount)

        displayUserInfo()

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }

        btnUpdatePass.setOnClickListener {
            updatePassword()
        }

        btnDeleteAccount.setOnClickListener {
            showDeleteDialog()
        }
    }

    private fun displayUserInfo() {
        val user = auth.currentUser!!

        tvEmail.text = "Email: ${user.email}"
        tvUid.text = "UID: ${user.uid.take(8)}..."

        val createdAt = user.metadata?.creationTimestamp ?: 0L
        val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(Date(createdAt))

        tvCreatedAt.text = "Member since: $date"
        tvInitial.text = user.email?.first()?.uppercaseChar()?.toString() ?: "?"
    }

    private fun updatePassword() {
        val newPass = etNewPassword.text.toString().trim()
        val confirmPass = etConfirmNewPass.text.toString().trim()

        if (newPass.length < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPass != confirmPass) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        auth.currentUser?.updatePassword(newPass)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password updated!", Toast.LENGTH_SHORT).show()
                    etNewPassword.text.clear()
                    etConfirmNewPass.text.clear()
                } else {
                    Toast.makeText(
                        this,
                        task.exception?.message ?: "Update failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to permanently delete your account? This cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                auth.currentUser?.delete()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finishAffinity()
                        } else {
                            Toast.makeText(
                                this,
                                task.exception?.message ?: "Delete failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

package com.example.lab12firebaseauth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

// HomeActivity — Login এর পরে দেখানো Dashboard
// Unauthenticated user এখানে আসতে পারবে না
class HomeActivity : AppCompatActivity() {

    private lateinit var tvEmail: TextView          // User এর email
    private lateinit var tvUid: TextView            // User এর UID (প্রথম ৮ টি character)
    private lateinit var tvCreatedAt: TextView      // Account creation date
    private lateinit var tvInitial: TextView        // Email এর প্রথম অক্ষর (Avatar)
    private lateinit var btnLogout: Button          // Logout button
    private lateinit var etNewPassword: EditText    // নতুন password
    private lateinit var etConfirmNewPass: EditText // Confirm নতুন password
    private lateinit var btnUpdatePass: Button      // Password update button
    private lateinit var btnDeleteAccount: Button   // Account delete button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()

        // যদি user logged in না থাকে — Login এ পাঠানো হচ্ছে
        // এটি Security check — সরাসরি HomeActivity open হলেও block করবে
        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        tvEmail         = findViewById(R.id.tvEmail)
        tvUid           = findViewById(R.id.tvUid)
        tvCreatedAt     = findViewById(R.id.tvCreatedAt)
        tvInitial       = findViewById(R.id.tvInitial)
        btnLogout       = findViewById(R.id.btnLogout)
        etNewPassword   = findViewById(R.id.etNewPassword)
        etConfirmNewPass = findViewById(R.id.etConfirmNewPass)
        btnUpdatePass   = findViewById(R.id.btnUpdatePass)
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount)

        // User info দেখানো হচ্ছে
        displayUserInfo()

        // Logout button
        btnLogout.setOnClickListener {
            auth.signOut() // Firebase থেকে sign out
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity() // সব screen বন্ধ
        }

        // Password update button
        btnUpdatePass.setOnClickListener { updatePassword() }

        // Delete Account button
        btnDeleteAccount.setOnClickListener { showDeleteDialog() }
    }

    // displayUserInfo — Logged in user এর তথ্য দেখানো
    private fun displayUserInfo() {
        val user = auth.currentUser!!

        // Email দেখানো
        tvEmail.text = "Email: ${user.email}"

        // UID এর প্রথম ৮ character দেখানো
        tvUid.text = "UID: ${user.uid.take(8)}..."

        // Account creation date format করা হচ্ছে
        val createdAt = user.metadata?.creationTimestamp ?: 0L
        val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(Date(createdAt))
        tvCreatedAt.text = "Member since: $date"

        // Email এর প্রথম অক্ষর Avatar হিসেবে দেখানো
        tvInitial.text = user.email?.first()?.uppercaseChar()?.toString() ?: "?"
    }

    // updatePassword — Firebase দিয়ে নতুন password set করা
    private fun updatePassword() {
        val newPass     = etNewPassword.text.toString().trim()
        val confirmPass = etConfirmNewPass.text.toString().trim()

        if (newPass.length < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            return
        }
        if (newPass != confirmPass) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // currentUser?.updatePassword — Firebase password update
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

    // showDeleteDialog — Account delete করার আগে confirmation নেওয়া
    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to permanently delete your account? This cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                // currentUser?.delete — Firebase থেকে account মুছে দেওয়া
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
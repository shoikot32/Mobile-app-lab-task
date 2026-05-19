package com.example.skyway

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.skyway.models.Booking
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PaymentActivity : AppCompatActivity() {

    private lateinit var etCardNumber: EditText
    private lateinit var etCardHolder: EditText
    private lateinit var etExpiry: EditText
    private lateinit var etCvv: EditText
    private lateinit var etMobileNumber: EditText
    private lateinit var autoCompleteProvider: AutoCompleteTextView
    private lateinit var btnPay: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvTotal: TextView
    private lateinit var togglePaymentMethod: MaterialButtonToggleGroup
    private lateinit var layoutCard: LinearLayout
    private lateinit var layoutMobile: LinearLayout

    private var totalAmount: Int = 0
    private var packageTitle: String? = null
    private var personCount: Int = 1
    private var selectedMethod: String = "Card"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Initialize Views
        etCardNumber = findViewById(R.id.etCardNumber)
        etCardHolder = findViewById(R.id.etCardHolder)
        etExpiry = findViewById(R.id.etExpiry)
        etCvv = findViewById(R.id.etCvv)
        etMobileNumber = findViewById(R.id.etMobileNumber)
        autoCompleteProvider = findViewById(R.id.autoCompleteProvider)
        btnPay = findViewById(R.id.btnPay)
        progressBar = findViewById(R.id.progressBar)
        tvTotal = findViewById(R.id.tvTotal)
        togglePaymentMethod = findViewById(R.id.togglePaymentMethod)
        layoutCard = findViewById(R.id.layoutCard)
        layoutMobile = findViewById(R.id.layoutMobile)

        totalAmount = intent.getIntExtra("total", 0)
        packageTitle = intent.getStringExtra("title")
        personCount = intent.getIntExtra("persons", 1)
        
        tvTotal.text = "৳ $totalAmount"

        setupProviderDropdown()
        setupToggleListener()

        btnPay.setOnClickListener {
            if (validateInputs()) {
                processPayment()
            }
        }
    }

    private fun setupProviderDropdown() {
        val providers = arrayOf("bKash", "Nagad", "Rocket", "Upay")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, providers)
        autoCompleteProvider.setAdapter(adapter)
    }

    private fun setupToggleListener() {
        togglePaymentMethod.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                if (checkedId == R.id.btnCard) {
                    selectedMethod = "Card"
                    layoutCard.visibility = View.VISIBLE
                    layoutMobile.visibility = View.GONE
                } else {
                    selectedMethod = "Mobile Banking"
                    layoutCard.visibility = View.GONE
                    layoutMobile.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        if (selectedMethod == "Card") {
            val cardNum = etCardNumber.text.toString()
            val holder = etCardHolder.text.toString()
            val expiry = etExpiry.text.toString()
            val cvv = etCvv.text.toString()

            if (cardNum.length < 16) {
                showError("Invalid Card Number")
                return false
            }
            if (holder.isEmpty()) {
                showError("Please enter Card Holder Name")
                return false
            }
            if (expiry.length < 5 || !expiry.contains("/")) {
                showError("Invalid Expiry Date (MM/YY)")
                return false
            }
            if (cvv.length < 3) {
                showError("Invalid CVV")
                return false
            }
        } else {
            val mobile = etMobileNumber.text.toString()
            val provider = autoCompleteProvider.text.toString()

            if (mobile.length < 11) {
                showError("Invalid Mobile Number")
                return false
            }
            if (provider.isEmpty()) {
                showError("Please select a provider")
                return false
            }
        }
        return true
    }

    private fun processPayment() {
        progressBar.visibility = View.VISIBLE
        btnPay.text = ""
        btnPay.isEnabled = false

        // Fetch User Details from DB then save booking
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        
        if (userId != null) {
            val userRef = FirebaseDatabase.getInstance("https://skyway-c4a96-default-rtdb.firebaseio.com/")
                .getReference("Users").child(userId)
            
            userRef.get().addOnSuccessListener { snapshot ->
                val userName = snapshot.child("username").value.toString()
                val fullName = snapshot.child("name").value.toString()
                val phone = snapshot.child("phone").value.toString()
                
                saveBookingToFirebase(userId, userName, fullName, phone)
            }.addOnFailureListener {
                saveBookingToFirebase(userId, "Unknown", "Unknown", "N/A")
            }
        } else {
            saveBookingToFirebase("Guest", "Guest", "Guest User", "N/A")
        }
    }

    private fun saveBookingToFirebase(userId: String, userName: String, fullName: String, phone: String) {
        val database = FirebaseDatabase.getInstance("https://skyway-c4a96-default-rtdb.firebaseio.com/")
            .getReference("bookings")
        
        val bookingId = database.push().key ?: System.currentTimeMillis().toString()

        val booking = Booking(
            bookingId = bookingId,
            userId = userId,
            userName = userName,
            fullName = fullName,
            phone = phone,
            packageTitle = packageTitle,
            totalAmount = totalAmount,
            personCount = personCount,
            paymentMethod = if (selectedMethod == "Card") "Card" else "Mobile (${autoCompleteProvider.text})",
            timestamp = System.currentTimeMillis(),
            status = "Confirmed"
        )

        database.child(bookingId).setValue(booking)
            .addOnSuccessListener {
                val intent = Intent(this, BookingSuccessActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                btnPay.text = "Secure Payment"
                btnPay.isEnabled = true
                showError("Failed to save booking: ${it.message}")
            }
    }

    private fun showError(message: String) {
        Snackbar.make(btnPay, message, Snackbar.LENGTH_SHORT).show()
    }
}

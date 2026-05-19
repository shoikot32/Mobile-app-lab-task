package com.example.skyway

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BookingActivity : AppCompatActivity() {

    private lateinit var tvPackageName: TextView
    private lateinit var tvInfo: TextView
    private lateinit var etPersons: EditText
    private lateinit var tvTotalBill: TextView
    private lateinit var btnNext: Button

    private var perPersonPrice: Int = 0
    private var packageTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        tvPackageName = findViewById(R.id.tvPackageName)
        tvInfo = findViewById(R.id.tvInfo)
        etPersons = findViewById(R.id.etPersons)
        tvTotalBill = findViewById(R.id.tvTotalBill)
        btnNext = findViewById(R.id.btnNext)

        packageTitle = intent.getStringExtra("title")
        perPersonPrice = intent.getIntExtra("price", 0)

        tvPackageName.text = packageTitle
        tvInfo.text = "Price per person: ৳$perPersonPrice"
        
        calculateTotal()

        etPersons.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateTotal()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnNext.setOnClickListener {
            val persons = etPersons.text.toString().toIntOrNull() ?: 1
            val total = persons * perPersonPrice

            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("title", packageTitle)
            intent.putExtra("total", total)
            intent.putExtra("persons", persons)
            startActivity(intent)
        }
    }

    private fun calculateTotal() {
        val persons = etPersons.text.toString().toIntOrNull() ?: 0
        val total = persons * perPersonPrice
        tvTotalBill.text = "৳ $total"
    }
}

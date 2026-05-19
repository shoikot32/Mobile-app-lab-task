package com.example.skyway.models

data class Booking(
    val bookingId: String? = null,
    val userId: String? = null,
    val packageTitle: String? = null,
    val totalAmount: Int = 0,
    val personCount: Int = 1,
    val paymentMethod: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Confirmed"
)
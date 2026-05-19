package com.example.skyway.models

data class TravelPackage(
    val title: String,
    val location: String,
    val price: Int,
    val days: Int,
    val image: String = "", // Changed to String for URL
    val description: String = "",
    val type: String = "City",
    val videoId: String = "L2p99ZNoKAc" // Default YouTube video ID
)
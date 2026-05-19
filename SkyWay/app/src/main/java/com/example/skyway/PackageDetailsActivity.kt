package com.example.skyway

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class PackageDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_details)

        val title = findViewById<TextView>(R.id.tvTitle)
        val location = findViewById<TextView>(R.id.tvLocation)
        val price = findViewById<TextView>(R.id.tvPrice)
        val days = findViewById<TextView>(R.id.tvDays)
        val description = findViewById<TextView>(R.id.tvDescription)
        val imageView = findViewById<ImageView>(R.id.img)
        val btnBook = findViewById<Button>(R.id.btnBookNow)
        val youtubePlayerView = findViewById<YouTubePlayerView>(R.id.youtubePlayer)

        val packageTitle = intent.getStringExtra("title")
        val packageLocation = intent.getStringExtra("location")
        val packagePrice = intent.getIntExtra("price", 0)
        val packageDays = intent.getIntExtra("days", 0)
        val packageDesc = intent.getStringExtra("description")
        val packageImg = intent.getStringExtra("image")
        val packageVideoId = intent.getStringExtra("videoId") ?: "L2p99ZNoKAc"

        title.text = packageTitle
        location.text = packageLocation
        price.text = "৳ $packagePrice"
        days.text = "$packageDays Days"
        description.text = packageDesc

        // Load image using Glide (fallback)
        Glide.with(this)
            .load(packageImg)
            .placeholder(R.drawable.skyway_logo)
            .into(imageView)

        // Initialize YouTube Player
        lifecycle.addObserver(youtubePlayerView)
        
        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                Log.d("YouTubePlayer", "Ready to load video: $packageVideoId")
                youTubePlayer.loadVideo(packageVideoId, 0f)
            }
            
            override fun onError(youTubePlayer: YouTubePlayer, error: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerError) {
                Log.e("YouTubePlayer", "Error loading video: $error")
            }
        })

        btnBook.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("title", packageTitle)
            intent.putExtra("price", packagePrice)
            startActivity(intent)
        }
    }
}

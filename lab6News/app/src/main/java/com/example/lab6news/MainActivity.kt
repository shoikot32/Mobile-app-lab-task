package com.example.lab6news

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView

class MainActivity : AppCompatActivity() {

    private var bookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nestedScroll = findViewById<NestedScrollView>(R.id.nestedScroll)
        val btnBookmark = findViewById<ImageButton>(R.id.btnBookmark)
        val btnShare = findViewById<ImageButton>(R.id.btnShare)
        val btnBackToTop = findViewById<Button>(R.id.btnBackToTop)

        val btnIntro = findViewById<Button>(R.id.btnIntro)
        val btnKeyPoints = findViewById<Button>(R.id.btnKeyPoints)
        val btnAnalysis = findViewById<Button>(R.id.btnAnalysis)
        val btnConclusion = findViewById<Button>(R.id.btnConclusion)

        val sectionIntro = findViewById<TextView>(R.id.sectionIntro)
        val sectionKey = findViewById<TextView>(R.id.sectionKeyPoints)
        val sectionAna = findViewById<TextView>(R.id.sectionAnalysis)
        val sectionCon = findViewById<TextView>(R.id.sectionConclusion)

        // Scroll helper
        fun scrollTo(view: TextView) {
            nestedScroll.post {
                nestedScroll.smoothScrollTo(0, view.top)
            }
        }

        // Section navigation
        btnIntro.setOnClickListener { scrollTo(sectionIntro) }
        btnKeyPoints.setOnClickListener { scrollTo(sectionKey) }
        btnAnalysis.setOnClickListener { scrollTo(sectionAna) }
        btnConclusion.setOnClickListener { scrollTo(sectionCon) }

        // Back to top
        btnBackToTop.setOnClickListener {
            nestedScroll.smoothScrollTo(0, 0)
        }

        // Bookmark toggle
        btnBookmark.setOnClickListener {
            bookmarked = !bookmarked

            btnBookmark.setImageResource(
                if (bookmarked)
                    android.R.drawable.btn_star_big_on
                else
                    android.R.drawable.btn_star_big_off
            )

            Toast.makeText(
                this,
                if (bookmarked) "Article Bookmarked" else "Bookmark Removed",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Share
        btnShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "The Future of AI in 2026")
            }
            startActivity(Intent.createChooser(intent, "Share via"))
        }
    }
}
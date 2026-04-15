package com.example.lab3fitnes

import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var tvSteps: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvPercent: TextView
    private lateinit var btnUpdateStats: Button
    private lateinit var tvCalories: TextView
    private lateinit var tvWater: TextView
    private lateinit var tvWorkoutTime: TextView

    private val dailyGoal = 10000
    private val calorieGoal = 320
    private val waterGoal = 2.5f
    private val workoutGoalMinutes = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvSteps = findViewById(R.id.tvSteps)
        progressBar = findViewById(R.id.progressBar)
        tvPercent = findViewById(R.id.tvPercent)
        btnUpdateStats = findViewById(R.id.btnUpdateStats)
        tvCalories = findViewById(R.id.tvCalories)
        tvWater = findViewById(R.id.tvWater)
        tvWorkoutTime = findViewById(R.id.tvWorkoutTime)

        progressBar.max = 100

        btnUpdateStats.setOnClickListener {
            showInputDialog()
        }
    }

    private fun showInputDialog() {
        val input = EditText(this)
        input.hint = "Enter step count"
        input.inputType = InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this)
            .setTitle("Update Steps")
            .setView(input)
            .setPositiveButton("Update") { _, _ ->
                val steps = input.text.toString().toIntOrNull()

                if (steps == null || steps < 0) {
                    Toast.makeText(this, "Enter valid steps", Toast.LENGTH_SHORT).show()
                } else {
                    updateProgress(steps)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateProgress(steps: Int) {

        // Steps
        tvSteps.text = String.format("%,d", steps)

        // Progress %
        val percent = ((steps.toFloat() / dailyGoal) * 100)
            .roundToInt()
            .coerceAtMost(100)

        progressBar.progress = percent
        tvPercent.text = "$percent%"

        // Calories
        val calories = ((steps.toFloat() / dailyGoal) * calorieGoal)
            .roundToInt()
        tvCalories.text = calories.toString()

        // Water
        val water = (steps.toFloat() / dailyGoal) * waterGoal
        tvWater.text = String.format("%.1f L", water)

        // ⏱️ Workout Time
        val minutes = ((steps.toFloat() / dailyGoal) * workoutGoalMinutes)
            .roundToInt()
        tvWorkoutTime.text = "$minutes min"

        // Feedback
        when {
            percent >= 100 -> Toast.makeText(this, "🎉 Goal reached!", Toast.LENGTH_SHORT).show()
            percent >= 75 -> Toast.makeText(this, "🔥 Almost there!", Toast.LENGTH_SHORT).show()
            percent >= 40 -> Toast.makeText(this, "💪 Keep going!", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, "🚶 Start moving!", Toast.LENGTH_SHORT).show()
        }
    }
}
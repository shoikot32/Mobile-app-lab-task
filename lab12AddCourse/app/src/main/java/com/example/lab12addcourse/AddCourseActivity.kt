package com.example.lab12addcourse

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class AddCourseActivity : AppCompatActivity() {

    private lateinit var etCourseName: EditText
    private lateinit var etCourseCode: EditText
    private lateinit var etInstructor: EditText
    private lateinit var spinnerCredits: Spinner
    private lateinit var etSchedule: EditText
    private lateinit var etRoom: EditText
    private lateinit var spinnerSemester: Spinner
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var progressBar: ProgressBar

    private val dbRef = FirebaseDatabase.getInstance().getReference("courses")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        supportActionBar?.title = "Add Course"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        etCourseName   = findViewById(R.id.etCourseName)
        etCourseCode   = findViewById(R.id.etCourseCode)
        etInstructor   = findViewById(R.id.etInstructor)
        spinnerCredits = findViewById(R.id.spinnerCredits)
        etSchedule     = findViewById(R.id.etSchedule)
        etRoom         = findViewById(R.id.etRoom)
        spinnerSemester = findViewById(R.id.spinnerSemester)
        btnSave        = findViewById(R.id.btnSave)
        btnCancel      = findViewById(R.id.btnCancel)
        progressBar    = findViewById(R.id.progressBar)

        val credits = listOf("1", "2", "3", "4")
        spinnerCredits.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, credits
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        val semesters = listOf("Spring 2025", "Summer 2025", "Fall 2025")
        spinnerSemester.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, semesters
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        btnSave.setOnClickListener   { saveCourse() }
        btnCancel.setOnClickListener { finish() }
    }

    private fun saveCourse() {
        val name     = etCourseName.text.toString().trim()
        val code     = etCourseCode.text.toString().trim()
        val instr    = etInstructor.text.toString().trim()
        val credits  = spinnerCredits.selectedItem.toString().toInt()
        val schedule = etSchedule.text.toString().trim()
        val room     = etRoom.text.toString().trim()
        val semester = spinnerSemester.selectedItem.toString()

        if (name.isEmpty() || code.isEmpty() || instr.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading(true)

        val newRef = dbRef.push()
        val courseId = newRef.key ?: ""

        val course = Course(
            id          = courseId,
            courseName  = name,
            courseCode  = code,
            instructor  = instr,
            creditHours = credits,
            schedule    = schedule,
            room        = room,
            semester    = semester
        )

        newRef.setValue(course)
            .addOnSuccessListener {
                showLoading(false)
                Toast.makeText(this, "Course added successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                showLoading(false)
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btnSave.isEnabled      = !show
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}


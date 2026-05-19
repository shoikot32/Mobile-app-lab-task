package com.example.lab12addcourse

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

// EditCourseActivity — Existing Course Update করার Screen
// Course object Intent এ পাওয়া যাচ্ছে আগের screen থেকে
class EditCourseActivity : AppCompatActivity() {

    private lateinit var etCourseName: EditText
    private lateinit var etCourseCode: EditText
    private lateinit var etInstructor: EditText
    private lateinit var spinnerCredits: Spinner
    private lateinit var etSchedule: EditText
    private lateinit var etRoom: EditText
    private lateinit var spinnerSemester: Spinner
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var progressBar: ProgressBar

    private val dbRef = FirebaseDatabase.getInstance().getReference("courses")
    private lateinit var currentCourse: Course // আগের screen থেকে আসা Course

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_course)

        supportActionBar?.title = "Edit Course"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Intent থেকে Course object নেওয়া হচ্ছে
        currentCourse = intent.getSerializableExtra("course") as? Course
            ?: run { finish(); return }

        etCourseName   = findViewById(R.id.etCourseName)
        etCourseCode   = findViewById(R.id.etCourseCode)
        etInstructor   = findViewById(R.id.etInstructor)
        spinnerCredits = findViewById(R.id.spinnerCredits)
        etSchedule     = findViewById(R.id.etSchedule)
        etRoom         = findViewById(R.id.etRoom)
        spinnerSemester = findViewById(R.id.spinnerSemester)
        btnUpdate      = findViewById(R.id.btnUpdate)
        btnDelete      = findViewById(R.id.btnDelete)
        progressBar    = findViewById(R.id.progressBar)

        // Spinner setup
        val credits = listOf("1", "2", "3", "4")
        spinnerCredits.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, credits
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        val semesters = listOf("Spring 2025", "Summer 2025", "Fall 2025")
        spinnerSemester.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, semesters
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        // Existing data pre-fill করা হচ্ছে
        prefillData()

        btnUpdate.setOnClickListener { updateCourse() }
        btnDelete.setOnClickListener { confirmDelete() }
    }

    // prefillData — আগের Course এর data fields এ বসানো হচ্ছে
    private fun prefillData() {
        etCourseName.setText(currentCourse.courseName)
        etCourseCode.setText(currentCourse.courseCode)
        etInstructor.setText(currentCourse.instructor)
        etSchedule.setText(currentCourse.schedule)
        etRoom.setText(currentCourse.room)

        // Spinner এ আগের value select করা হচ্ছে
        val creditList = listOf("1", "2", "3", "4")
        spinnerCredits.setSelection(
            creditList.indexOf(currentCourse.creditHours.toString()).coerceAtLeast(0)
        )

        val semList = listOf("Spring 2025", "Summer 2025", "Fall 2025")
        spinnerSemester.setSelection(
            semList.indexOf(currentCourse.semester).coerceAtLeast(0)
        )
    }

    // updateCourse — Firebase এ existing node update করা
    private fun updateCourse() {
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

        // Updated Course object তৈরি
        val updatedCourse = Course(
            id          = currentCourse.id,
            courseName  = name,
            courseCode  = code,
            instructor  = instr,
            creditHours = credits,
            schedule    = schedule,
            room        = room,
            semester    = semester
        )

        // Firebase এ same ID তে setValue() দিয়ে update করা হচ্ছে
        // child(currentCourse.id) — same node এ যাচ্ছি
        dbRef.child(currentCourse.id).setValue(updatedCourse)
            .addOnSuccessListener {
                showLoading(false)
                Toast.makeText(this, "Course updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                showLoading(false)
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // confirmDelete — Delete করার আগে confirmation
    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Delete Course")
            .setMessage("Delete ${currentCourse.courseName} permanently?")
            .setPositiveButton("Delete") { _, _ ->
                dbRef.child(currentCourse.id).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btnUpdate.isEnabled    = !show
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

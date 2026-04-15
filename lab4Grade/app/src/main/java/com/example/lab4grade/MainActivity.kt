package com.example.lab4grade

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gradeTable: TableLayout
    private lateinit var tvSummary: TextView
    private lateinit var tvGPA: TextView
    private lateinit var tvStudentInfo: TextView

    private lateinit var etStudentId: EditText
    private lateinit var etSubject: EditText
    private lateinit var etObtained: EditText
    private lateinit var etTotal: EditText

    private lateinit var btnAdd: Button
    private lateinit var btnShare: Button

    private var totalSubjects = 0
    private var passed = 0
    private var failed = 0
    private var totalGradePoints = 0.0

    private var studentId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gradeTable = findViewById(R.id.gradeTable)
        tvSummary = findViewById(R.id.tvSummary)
        tvGPA = findViewById(R.id.tvGPA)
        tvStudentInfo = findViewById(R.id.tvStudentInfo)

        etStudentId = findViewById(R.id.etStudentId)
        etSubject = findViewById(R.id.etSubject)
        etObtained = findViewById(R.id.etObtained)
        etTotal = findViewById(R.id.etTotal)

        btnAdd = findViewById(R.id.btnAdd)
        btnShare = findViewById(R.id.btnShare)

        btnAdd.setOnClickListener {
            addSubject()
        }

        btnShare.setOnClickListener {
            shareReport()
        }
    }

    private fun addSubject() {

        // 🔴 Get ID only once
        if (studentId.isEmpty()) {
            studentId = etStudentId.text.toString()

            if (studentId.isEmpty()) {
                Toast.makeText(this, "Enter Student ID first", Toast.LENGTH_SHORT).show()
                return
            }

            tvStudentInfo.text = "Student ID: $studentId"
        }

        val subject = etSubject.text.toString()
        val obtained = etObtained.text.toString().toIntOrNull()
        val total = etTotal.text.toString().toIntOrNull()

        if (subject.isEmpty() || obtained == null || total == null || total == 0) {
            Toast.makeText(this, "Enter valid subject data", Toast.LENGTH_SHORT).show()
            return
        }

        val percent = (obtained * 100) / total
        val (grade, point, isPass) = calculateGrade(percent)

        totalSubjects++
        if (isPass) passed++ else failed++
        totalGradePoints += point

        val row = TableRow(this)

        row.addView(createCell(subject))
        row.addView(createCell("$obtained"))
        row.addView(createCell("$total"))
        row.addView(createCell(grade))

        gradeTable.addView(row)

        updateSummary()

        etSubject.text.clear()
        etObtained.text.clear()
        etTotal.text.clear()
    }

    private fun calculateGrade(percent: Int): Triple<String, Double, Boolean> {
        return when {
            percent >= 80 -> Triple("A+", 4.0, true)
            percent >= 70 -> Triple("A", 3.7, true)
            percent >= 60 -> Triple("A-", 3.5, true)
            percent >= 50 -> Triple("B", 3.0, true)
            percent >= 40 -> Triple("C", 2.0, true)
            else -> Triple("F", 0.0, false)
        }
    }

    private fun updateSummary() {
        tvSummary.text = "Total: $totalSubjects | Passed: $passed | Failed: $failed"

        val gpa = if (totalSubjects > 0) totalGradePoints / totalSubjects else 0.0
        tvGPA.text = "GPA: %.2f".format(gpa)
    }

    private fun createCell(text: String): TextView {
        val tv = TextView(this)
        tv.text = text
        tv.setPadding(8, 8, 8, 8)
        return tv
    }

    private fun shareReport() {
        val report = """
            Student ID: $studentId
            
            $tvSummary
            ${tvGPA.text}
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, report)

        startActivity(Intent.createChooser(intent, "Share via"))
    }
}
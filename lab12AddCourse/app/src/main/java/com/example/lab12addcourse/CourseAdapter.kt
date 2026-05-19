package com.example.lab12addcourse

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class CourseAdapter(
    private var courses: MutableList<Course>,
    private val onEdit: (Course) -> Unit,
    private val onDelete: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCourseName: TextView  = view.findViewById(R.id.tvCourseName)
        val tvCourseCode: TextView  = view.findViewById(R.id.tvCourseCode)
        val tvInstructor: TextView  = view.findViewById(R.id.tvInstructor)
        val tvCreditHours: TextView = view.findViewById(R.id.tvCreditHours)
        val tvSchedule: TextView    = view.findViewById(R.id.tvSchedule)
        val btnEdit: ImageButton    = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton  = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]

        holder.tvCourseName.text  = course.courseName
        holder.tvCourseCode.text  = course.courseCode
        holder.tvInstructor.text  = "Instructor: ${course.instructor}"
        holder.tvCreditHours.text = "${course.creditHours} Credits"
        holder.tvSchedule.text    = course.schedule

        holder.btnEdit.setOnClickListener { onEdit(course) }

        holder.btnDelete.setOnClickListener { onDelete(course) }
    }

    override fun getItemCount() = courses.size

    fun updateList(newList: MutableList<Course>) {
        courses = newList
        notifyDataSetChanged()
    }
}


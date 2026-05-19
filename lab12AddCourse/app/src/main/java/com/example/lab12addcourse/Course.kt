package com.example.lab12addcourse

import java.io.Serializable

data class Course(
    var id: String = "",
    val courseName: String = "",
    val courseCode: String = "",
    val instructor: String = "",
    val creditHours: Int = 3,
    val schedule: String = "",
    val room: String = "",
    val semester: String = ""
) : Serializable


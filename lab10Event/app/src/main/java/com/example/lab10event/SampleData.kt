package com.example.lab10event

object SampleData {

    val events = mutableListOf(

        Event(
            "Tech Fest 2026",        // title
            "10 Oct",                // date
            "10:00 AM",              // time
            "Auditorium",            // venue
            "Tech",                  // category
            "Big tech event",        // description
            R.mipmap.ic_launcher,    // imageRes (INT)
            50,                      // seats
            200.0                    // price
        ),

        Event(
            "Football Match",
            "12 Oct",
            "4:00 PM",
            "Stadium",
            "Sports",
            "Inter-department match",
            R.mipmap.ic_launcher,
            100,
            100.0
        )
    )
}
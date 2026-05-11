package com.example.lab10event

object SampleData {
    val events = mutableListOf(
        Event(
            id = 1,
            title = "AI Summit 2026",
            date = "2026-05-10",
            time = "10:00 AM",
            venue = "Hall A",
            category = "Tech",
            description = "Explore the latest breakthroughs in artificial intelligence and machine learning. Industry experts and researchers will present cutting-edge work in deep learning, natural language processing, and AI ethics. Networking sessions and live demos included.",
            price = 300.0,
            totalSeats = 100,
            availableSeats = 60,
            imageRes = R.mipmap.ic_launcher
        ),
        Event(
            id = 2,
            title = "Football Finals",
            date = "2026-05-12",
            time = "03:00 PM",
            venue = "University Stadium",
            category = "Sports",
            description = "Annual university football championship finals. Watch the top two department teams compete for the golden trophy. Free entry for all students with valid ID card.",
            price = 0.0,
            totalSeats = 500,
            availableSeats = 200,
            imageRes = R.mipmap.ic_launcher
        ),
        Event(
            id = 3,
            title = "Cultural Night",
            date = "2026-05-15",
            time = "06:00 PM",
            venue = "Main Auditorium",
            category = "Cultural",
            description = "A spectacular celebration of diverse cultures and traditions from around the world. Enjoy music, dance, food, and art performances by student groups representing over 20 countries.",
            price = 50.0,
            totalSeats = 300,
            availableSeats = 150,
            imageRes = R.mipmap.ic_launcher
        ),
        Event(
            id = 4,
            title = "Research Expo",
            date = "2026-05-18",
            time = "09:00 AM",
            venue = "Lab Block",
            category = "Academic",
            description = "Students and faculty present their latest research projects across all disciplines. Judges from industry and academia will evaluate submissions. Best projects win funding grants.",
            price = 0.0,
            totalSeats = 200,
            availableSeats = 100,
            imageRes = R.mipmap.ic_launcher
        ),
        Event(
            id = 5,
            title = "Music Fest",
            date = "2026-05-20",
            time = "05:00 PM",
            venue = "Open Ground",
            category = "Social",
            description = "Live performances by university bands and guest artists. Food stalls, art installations, and activity zones. A night to remember with lights, music, and great vibes.",
            price = 100.0,
            totalSeats = 1000,
            availableSeats = 400,
            imageRes = R.mipmap.ic_launcher
        ),
        Event(
            id = 6,
            title = "Hackathon 2026",
            date = "2026-05-22",
            time = "08:00 AM",
            venue = "IT Block",
            category = "Tech",
            description = "A 24-hour coding competition where teams of 3-4 build innovative solutions to real-world problems. Exciting prizes worth over BDT 1,00,000. Open to all CSE and EEE students.",
            price = 200.0,
            totalSeats = 150,
            availableSeats = 80,
            imageRes = R.mipmap.ic_launcher
        ),
        Event(
            id = 7,
            title = "Cricket Tournament",
            date = "2026-05-25",
            time = "02:00 PM",
            venue = "Cricket Ground",
            category = "Sports",
            description = "Inter-department cricket tournament with 8 teams competing over 3 days. Finals on the last day with prize distribution ceremony. Come support your department team.",
            price = 0.0,
            totalSeats = 400,
            availableSeats = 300,
            imageRes = R.mipmap.ic_launcher
        ),
        Event(
            id = 8,
            title = "Career Fair",
            date = "2026-05-28",
            time = "10:00 AM",
            venue = "Convention Center",
            category = "Academic",
            description = "Meet top employers from over 50 companies. Bring your CV, attend mock interviews, and explore internship and full-time job opportunities. Dress code: formal.",
            price = 0.0,
            totalSeats = 600,
            availableSeats = 350,
            imageRes = R.mipmap.ic_launcher
        )
    )
}
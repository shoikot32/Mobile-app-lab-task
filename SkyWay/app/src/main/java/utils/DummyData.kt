package com.example.skyway.utils

import com.example.skyway.models.TravelPackage

object DummyData {

    val packageList = listOf(
        TravelPackage("Dubai Luxury Tour", "Dubai, UAE", 85000, 5, "https://images.unsplash.com/photo-1512453979798-5ea266f8880c?w=600", "Experience the ultimate luxury in Dubai. Visit the Burj Khalifa, enjoy a desert safari, and shop at the world's largest malls.", "Luxury", "L2p99ZNoKAc"),
        TravelPackage("Thailand Beach Tour", "Phuket, Thailand", 65000, 4, "https://images.unsplash.com/photo-1528127269322-539801943592?w=600", "Relax on the beautiful beaches of Phuket. Enjoy water sports, vibrant nightlife, and delicious Thai cuisine.", "Beach", "68X-n_J0YtE"),
        TravelPackage("Maldives Honeymoon", "Maldives", 120000, 5, "https://images.unsplash.com/photo-1514282401047-d79a71a590e8?w=600", "A perfect romantic getaway. Stay in overwater villas, swim in crystal clear waters, and enjoy private dinners by the sea.", "Honeymoon", "f26966mO7oA"),
        TravelPackage("Singapore City Tour", "Singapore", 78000, 4, "https://images.unsplash.com/photo-1525596662741-e94ff9f26de1?w=600", "Explore the futuristic city of Singapore. Visit Gardens by the Bay, Sentosa Island, and Universal Studios.", "City", "8xJvC86pCGo"),
        TravelPackage("Malaysia Adventure", "Kuala Lumpur, Malaysia", 55000, 4, "https://images.unsplash.com/photo-1567121938596-9d9d03493ed2?w=600", "Discover the cultural diversity of Malaysia. Visit the Petronas Twin Towers and explore the Batu Caves.", "Adventure", "pP901Z_T_z4")
    ).let { list ->
        val fullList = mutableListOf<TravelPackage>()
        for (i in 0 until 20) {
            list.forEach { pkg ->
                fullList.add(pkg.copy(
                    title = if (i == 0) pkg.title else "${pkg.title} (Option ${i+1})",
                    image = "https://picsum.photos/seed/${pkg.title.hashCode() + i}/600/400"
                ))
            }
        }
        fullList.take(100)
    }
}
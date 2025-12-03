package com.example.sports.data.remote.dto

//Firestore requires default values
data class SportPerformanceDto(
    val id: String = "",
    val name: String = "",
    val durationMinutes: Int = 0,
    val location: String = "",
    val venue: String = ""
)

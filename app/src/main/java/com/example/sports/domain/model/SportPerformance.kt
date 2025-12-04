package com.example.sports.domain.model

import java.util.UUID

data class SportPerformance(
    val id : String = UUID.randomUUID().toString(),
    val name: String,
    val venue: String,
    val durationMinutes: Int,
    val storageType: StorageType
)

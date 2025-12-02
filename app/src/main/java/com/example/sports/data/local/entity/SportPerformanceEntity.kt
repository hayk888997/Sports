package com.example.sports.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sports_performance")
data class SportPerformanceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val venue: String,
    val durationMinutes: Int
)

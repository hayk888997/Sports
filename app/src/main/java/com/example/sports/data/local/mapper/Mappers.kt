package com.example.sports.data.local.mapper

import com.example.sports.data.local.entity.SportPerformanceEntity
import com.example.sports.domain.model.SportPerformance
import com.example.sports.domain.model.StorageType

fun SportPerformance.toEntity() = SportPerformanceEntity(
    id = id,
    name = name,
    venue = venue,
    durationMinutes = durationMinutes
)

fun SportPerformanceEntity.toDomain() = SportPerformance(
    id = id,
    name = name,
    venue = venue,
    durationMinutes = durationMinutes,
    storageType = StorageType.LOCAL
)

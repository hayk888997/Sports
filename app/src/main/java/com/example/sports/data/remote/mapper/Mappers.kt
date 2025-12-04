package com.example.sports.data.remote.mapper

import com.example.sports.data.remote.dto.SportPerformanceDto
import com.example.sports.domain.model.SportPerformance
import com.example.sports.domain.model.StorageType

fun SportPerformance.toDto() = SportPerformanceDto(
    id = id,
    name = name,
    venue = venue,
    durationMinutes = durationMinutes
)

fun SportPerformanceDto.toDomain() = SportPerformance(
    id = id,
    name = name,
    venue = venue,
    durationMinutes = durationMinutes,
    storageType = StorageType.REMOTE
)

package com.example.sports.data.remote.mapper

import com.example.sports.data.remote.dto.SportPerformanceDto
import com.example.sports.domain.model.SportPerformance
import com.example.sports.domain.model.StorageType

//In a big project I would separate mappers, for now doesn't make sense
fun SportPerformance.toDto() = SportPerformanceDto(
    id = id,
    name = name,
    location = location,
    durationMinutes = durationMinutes
)

fun SportPerformanceDto.toDomain() = SportPerformance(
    id = id,
    name = name,
    location = location,
    durationMinutes = durationMinutes,
    storageType = StorageType.REMOTE
)

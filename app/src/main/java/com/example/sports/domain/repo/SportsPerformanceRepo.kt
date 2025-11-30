package com.example.sports.domain.repo

import com.example.sports.domain.model.FilterType
import com.example.sports.domain.model.SportPerformance
import com.example.sports.domain.model.StorageType

interface SportsPerformanceRepo {
    suspend fun getPerformances(filterType: FilterType): List<SportPerformance>
    suspend fun storeSportPerformance(performance: SportPerformance, storageType: StorageType)
}

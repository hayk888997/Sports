package com.example.sports.domain.repo

import com.example.sports.domain.model.FilterType
import com.example.sports.domain.model.SportPerformance
import com.example.sports.domain.model.StorageType
import com.example.sports.domain.util.Result

interface SportsPerformanceRepo {

//    After thinking, doesn't make sense. But leaving for showcasing a bit :)
//    fun observePerformances(): Flow<Result<List<SportPerformance>>>

    suspend fun getPerformances(filterType: FilterType): Result<List<SportPerformance>>
    suspend fun storeSportPerformance(
        performance: SportPerformance,
        storageType: StorageType
    ): Result<Unit>
}

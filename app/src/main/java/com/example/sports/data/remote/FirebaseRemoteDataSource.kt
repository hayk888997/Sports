package com.example.sports.data.remote

import com.example.sports.data.remote.dto.SportPerformanceDto
import kotlinx.coroutines.flow.Flow


class FirebaseRemoteDataSource(
    private val firebaseService: FirebaseService
) {
    fun observePerformances(): Flow<List<SportPerformanceDto>> = firebaseService.observePerformances()

    suspend fun getPerformances(): List<SportPerformanceDto> =
        firebaseService.getAllPerformances()

    suspend fun savePerformance(performance: SportPerformanceDto) {
        firebaseService.insertPerformance(performance)
    }
}

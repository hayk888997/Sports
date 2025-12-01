package com.example.sports.domain.usecase

import com.example.sports.domain.model.SportPerformance
import com.example.sports.domain.model.StorageType
import com.example.sports.domain.repo.SportsPerformanceRepo
import com.example.sports.domain.util.Result

class StoreSportPerformanceUseCase(
    val repo: SportsPerformanceRepo
) {
    suspend operator fun invoke(performance: SportPerformance, storageType: StorageType): Result<Unit> {
        return repo.storeSportPerformance(performance, storageType)
    }
}

package com.example.sports.domain.usecase

import com.example.sports.domain.model.FilterType
import com.example.sports.domain.repo.SportsPerformanceRepo

class GetSportsPerformancesUseCase(
    val repo: SportsPerformanceRepo
) {

    suspend operator fun invoke(filterType: FilterType) {
        repo.getPerformances(filterType)
    }
}

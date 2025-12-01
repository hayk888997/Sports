package com.example.sports.domain.usecase

import com.example.sports.domain.model.FilterType
import com.example.sports.domain.model.SportPerformance
import com.example.sports.domain.repo.SportsPerformanceRepo
import com.example.sports.domain.util.Result

class GetSportsPerformancesUseCase(
    val repo: SportsPerformanceRepo
) {

    suspend operator fun invoke(filterType: FilterType): Result<List<SportPerformance>> {
        return repo.getPerformances(filterType)
    }
}

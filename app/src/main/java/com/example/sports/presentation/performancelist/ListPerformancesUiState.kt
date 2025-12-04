package com.example.sports.presentation.performancelist

import com.example.sports.domain.model.FilterType
import com.example.sports.domain.model.SportPerformance
import com.example.sports.presentation.commmon.AppError

data class ListPerformancesUiState(
    val localPerformances: List<SportPerformance> = emptyList(),
    val remotePerformances: List<SportPerformance> = emptyList(),
    val allPerformances: List<SportPerformance> = emptyList(),
    val selectedFilter: FilterType = FilterType.ALL,
    val isLoading: Boolean = false,
    val error: AppError? = null,
    val scrollState: ListScrollState = ListScrollState()
)

data class ListScrollState(
    val all: Int = 0,
    val local: Int = 0,
    val remote: Int = 0
)

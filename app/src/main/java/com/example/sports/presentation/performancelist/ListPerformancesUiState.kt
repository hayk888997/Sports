package com.example.sports.presentation.performancelist

import com.example.sports.domain.model.FilterType
import com.example.sports.domain.model.SportPerformance

data class ListPerformancesUiState(
    val performances: List<SportPerformance> = emptyList(),
    val selectedFilter: FilterType = FilterType.ALL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

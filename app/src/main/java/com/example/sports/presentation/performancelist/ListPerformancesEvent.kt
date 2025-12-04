package com.example.sports.presentation.performancelist

import com.example.sports.domain.model.FilterType

sealed class ListPerformancesEvent {
    data class FilterChanged(val filter: FilterType) : ListPerformancesEvent()
    object Refresh : ListPerformancesEvent()
    data class ScrollPositionChanged(val position: Int) : ListPerformancesEvent()
}

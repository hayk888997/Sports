package com.example.sports.presentation.insertperformance

import com.example.sports.domain.model.StorageType

sealed class InsertPerformanceEvent {
    data class NameChanged(val name: String) : InsertPerformanceEvent()
    data class VenueChanged(val venue: String) : InsertPerformanceEvent()
    data class DurationChanged(val duration: String) : InsertPerformanceEvent()
    data class StorageTypeChanged(val storageType: StorageType) : InsertPerformanceEvent()
    object Submit : InsertPerformanceEvent()
    object ErrorConsumed : InsertPerformanceEvent()
}

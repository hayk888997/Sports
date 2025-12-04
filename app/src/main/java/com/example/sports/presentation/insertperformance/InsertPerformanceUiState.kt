package com.example.sports.presentation.insertperformance

import com.example.sports.domain.model.StorageType
import com.example.sports.presentation.commmon.AppError

data class InsertPerformanceUiState(
    val name: String = "",
    val venue: String = "",
    val duration: String = "",
    val storageType: StorageType = StorageType.LOCAL,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: AppError? = null
)



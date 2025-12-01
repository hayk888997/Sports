package com.example.sports.presentation.insertperformance

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sports.domain.model.SportPerformance
import com.example.sports.domain.usecase.StoreSportPerformanceUseCase
import com.example.sports.domain.util.DataError
import kotlinx.coroutines.launch
import com.example.sports.domain.util.Result

class InsertPerformanceViewModel(
    private val saveUseCase: StoreSportPerformanceUseCase
) : ViewModel() {

    var uiState by mutableStateOf(InsertPerformanceUiState())
        private set

    fun onEvent(event: InsertPerformanceEvent) {
        when (event) {
            is InsertPerformanceEvent.NameChanged -> {
                uiState = uiState.copy(name = event.name)
            }

            is InsertPerformanceEvent.LocationChanged -> {
                uiState = uiState.copy(location = event.location)
            }

            is InsertPerformanceEvent.DurationChanged -> {
                uiState = uiState.copy(duration = event.duration)
            }

            is InsertPerformanceEvent.StorageTypeChanged -> {
                uiState = uiState.copy(storageType = event.storageType)
            }

            InsertPerformanceEvent.ErrorConsumed -> {
                uiState = uiState.copy(errorMessage = null)
            }

            InsertPerformanceEvent.Submit -> {
                submitForm()
            }
        }
    }

    private fun submitForm() {
        val duration = uiState.duration.toIntOrNull()
        if (uiState.name.isBlank() || uiState.location.isBlank() || duration == null) {
            uiState = uiState.copy(errorMessage = "Please fill all fields correctly")
            return
        }

        val performance = SportPerformance(
            name = uiState.name,
            location = uiState.location,
            durationMinutes = duration,
            storageType = uiState.storageType
        )

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, success = false, errorMessage = null)

            val result = saveUseCase(performance, uiState.storageType)
            uiState = when (result) {
                is Result.Success -> uiState.copy(isLoading = false, success = true)
                is Result.Error -> uiState.copy(
                    isLoading = false,
                    errorMessage = mapError(result.error)
                )
            }
        }
    }

    private fun mapError(error: DataError): String = when (error) {
        DataError.Network -> "Network error"
        DataError.Database -> "Database error"
        DataError.NotFound -> "Not found"
        is DataError.Unknown -> "Unknown error: ${error.message}"
    }
}

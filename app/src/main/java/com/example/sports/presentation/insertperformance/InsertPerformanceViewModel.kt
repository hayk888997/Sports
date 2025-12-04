package com.example.sports.presentation.insertperformance

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sports.domain.model.SportPerformance
import com.example.sports.domain.usecase.StoreSportPerformanceUseCase
import kotlinx.coroutines.launch
import com.example.sports.domain.util.Result
import com.example.sports.presentation.commmon.AppError
import com.example.sports.presentation.commmon.toAppError

class InsertPerformanceViewModel(
    private val saveUseCase: StoreSportPerformanceUseCase
) : ViewModel() {

    var uiState by mutableStateOf(InsertPerformanceUiState())
        private set

    init {
        uiState = InsertPerformanceUiState()
    }

    fun onEvent(event: InsertPerformanceEvent) {
        when (event) {
            is InsertPerformanceEvent.NameChanged -> {
                uiState = uiState.copy(name = event.name)
            }

            is InsertPerformanceEvent.VenueChanged -> {
                uiState = uiState.copy(venue = event.venue)
            }

            is InsertPerformanceEvent.DurationChanged -> {
                uiState = uiState.copy(duration = event.duration)
            }

            is InsertPerformanceEvent.StorageTypeChanged -> {
                uiState = uiState.copy(storageType = event.storageType)
            }

            InsertPerformanceEvent.ErrorConsumed -> {
                uiState = uiState.copy(error = null)
            }

            InsertPerformanceEvent.Submit -> {
                submitForm()
            }

            InsertPerformanceEvent.OnSuccessHandled -> {
                uiState = InsertPerformanceUiState()
            }
        }
    }

    private fun submitForm() {
        val duration = uiState.duration.toIntOrNull()
        when {
            uiState.name.isBlank() -> {
                uiState = uiState.copy(error = AppError.EmptyName)
                return
            }

            uiState.venue.isBlank() -> {
                uiState = uiState.copy(error = AppError.EmptyVenue)
                return
            }

            duration == null -> {
                uiState = uiState.copy(error = AppError.InvalidDuration)
                return
            }

            duration <= 0 -> {
                uiState = uiState.copy(error = AppError.NegativeDuration)
                return
            }
        }

        val performance = SportPerformance(
            name = uiState.name,
            venue = uiState.venue,
            durationMinutes = duration,
            storageType = uiState.storageType
        )

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, success = false, error = null)

            val result = saveUseCase(performance, uiState.storageType)
            uiState = when (result) {
                is Result.Success -> uiState.copy(isLoading = false, success = true)
                is Result.Error -> uiState.copy(
                    isLoading = false,
                    error = result.error.toAppError()
                )
            }
        }
    }
}

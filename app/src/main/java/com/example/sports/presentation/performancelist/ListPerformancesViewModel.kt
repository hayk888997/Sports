package com.example.sports.presentation.performancelist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sports.domain.usecase.GetSportsPerformancesUseCase
import com.example.sports.domain.util.DataError
import kotlinx.coroutines.launch
import com.example.sports.domain.util.Result

class ListPerformancesViewModel(
    private val getUseCase: GetSportsPerformancesUseCase
) : ViewModel() {

    var uiState by mutableStateOf(ListPerformancesUiState())
        private set

    init {
        loadPerformances()
    }

    fun onEvent(event: ListPerformancesEvent) {
        when (event) {
            is ListPerformancesEvent.FilterChanged -> {
                uiState = uiState.copy(selectedFilter = event.filter)
                loadPerformances()
            }

            ListPerformancesEvent.Refresh -> {
                loadPerformances()
            }

            ListPerformancesEvent.ErrorConsumed -> {
                uiState = uiState.copy(errorMessage = null)
            }
        }
    }

    private fun loadPerformances() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)

            val result = getUseCase(uiState.selectedFilter)
            uiState = when (result) {
                is Result.Success -> uiState.copy(
                    performances = result.data,
                    isLoading = false
                )

                is Result.Error -> uiState.copy(
                    isLoading = false,
                    errorMessage = mapError(result.error)
                )
            }
        }
    }

    private fun mapError(error: DataError): String = when (error) {
        DataError.Network -> "Network error while loading"
        DataError.Database -> "Local DB error"
        DataError.NotFound -> "No data found"
        is DataError.Unknown -> "Unexpected error: ${error.message}"
    }
}

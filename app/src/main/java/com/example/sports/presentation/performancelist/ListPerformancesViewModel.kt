package com.example.sports.presentation.performancelist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sports.domain.model.FilterType
import com.example.sports.domain.model.StorageType
import com.example.sports.domain.usecase.GetSportsPerformancesUseCase
import kotlinx.coroutines.launch
import com.example.sports.domain.util.Result
import com.example.sports.presentation.commmon.toAppError

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
            }

            ListPerformancesEvent.Refresh -> {
                loadPerformances()
            }

            is ListPerformancesEvent.ScrollPositionChanged -> {
                val newScrollState = when (uiState.selectedFilter) {
                    FilterType.ALL -> uiState.scrollState.copy(all = event.position)
                    FilterType.LOCAL -> uiState.scrollState.copy(local = event.position)
                    FilterType.REMOTE -> uiState.scrollState.copy(remote = event.position)
                }
                uiState = uiState.copy(scrollState = newScrollState)
            }
        }
    }

    private fun loadPerformances() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)

            val allPerformancesResult = getUseCase(FilterType.ALL)

            uiState = when (allPerformancesResult) {
                is Result.Success -> {
                    val allPerformances = allPerformancesResult.data
                    val localPerformances =
                        allPerformances.filter { it.storageType == StorageType.LOCAL }
                    val remotePerformances =
                        allPerformances.filter { it.storageType == StorageType.REMOTE }
                    uiState.copy(
                        allPerformances = allPerformances,
                        localPerformances = localPerformances,
                        remotePerformances = remotePerformances,
                        isLoading = false
                    )
                }

                is Result.Error -> uiState.copy(
                    isLoading = false,
                    error = allPerformancesResult.error.toAppError()
                )
            }
        }
    }
}

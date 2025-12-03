package com.example.sports.presentation.performancelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import com.example.sports.presentation.ui.components.ErrorSnackBar
import com.example.sports.presentation.ui.components.FilterChips
import com.example.sports.presentation.ui.components.LoadingIndicator
import com.example.sports.presentation.ui.components.PerformanceList
import com.example.sports.presentation.ui.theme.ExtraColors
import com.example.sports.presentation.ui.theme.LocalExtraColors

@Composable
fun ListPerformancesScreen(
    uiState: ListPerformancesUiState,
    onEvent: (ListPerformancesEvent) -> Unit,
    onNavigateToInsert: () -> Unit,
) {
    val extraColors = LocalExtraColors.current

    Scaffold(
        containerColor = extraColors.screenBg,
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToInsert) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Column(modifier =
            Modifier
                .padding(padding)
                .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            FilterChips(
                selected = uiState.selectedFilter,
                onFilterSelected = { onEvent(ListPerformancesEvent.FilterChanged(it)) }
            )

            Spacer(Modifier.height(16.dp))

            Content(
                state = uiState,
                extraColors = extraColors,
                onRefresh = { onEvent(ListPerformancesEvent.Refresh) }
            )
        }
    }
}

@Composable
private fun Content(
    state: ListPerformancesUiState,
    extraColors: ExtraColors,
    onRefresh: () -> Unit
) {
    when {
        state.isLoading -> LoadingIndicator()

        state.errorMessage != null -> ErrorSnackBar(state.errorMessage, extraColors)

        else -> PerformanceList(
            items = state.performances,
            extraColors = extraColors,
            onRefresh = onRefresh
        )
    }
}

@Composable
@Preview
fun ListPerformancesScreenPreview() {
    ListPerformancesScreen(ListPerformancesUiState(), {}, {})
}

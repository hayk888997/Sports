package com.example.sports.presentation.performancelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.sports.R
import com.example.sports.presentation.commmon.components.ErrorSnackBar
import com.example.sports.presentation.commmon.components.FilterChips
import com.example.sports.presentation.commmon.components.LoadingIndicator
import com.example.sports.presentation.commmon.errorTextFor
import com.example.sports.presentation.commmon.isLandscape
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
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToInsert) {
                Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = null)
            }
        }
    ) { padding ->
        if (isLandscape()) {
            Landscape(
                Modifier.padding(padding),
                state = uiState,
                extraColors = extraColors,
                onEvent
            )
        } else {
            Portrait(
                Modifier.padding(padding),
                state = uiState,
                extraColors = extraColors,
                onEvent
            )
        }
    }
}

@Composable
private fun Landscape(
    modifier: Modifier = Modifier,
    state: ListPerformancesUiState,
    extraColors: ExtraColors,
    onEvent: (ListPerformancesEvent) -> Unit,
) {
    Row(
        modifier = modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        FilterChips(
            selected = state.selectedFilter,
            onFilterSelected = { onEvent(ListPerformancesEvent.FilterChanged(it)) }
        )

        Content(
            state = state,
            extraColors = extraColors,
            onEvent = onEvent
        )
    }
}

@Composable
private fun Portrait(
    modifier: Modifier = Modifier,
    state: ListPerformancesUiState,
    extraColors: ExtraColors,
    onEvent: (ListPerformancesEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        FilterChips(
            selected = state.selectedFilter,
            onFilterSelected = { onEvent(ListPerformancesEvent.FilterChanged(it)) }
        )

        Spacer(Modifier.height(16.dp))

        Content(
            state = state,
            extraColors = extraColors,
            onEvent = onEvent
        )
    }
}

@Composable
private fun Content(
    state: ListPerformancesUiState,
    extraColors: ExtraColors,
    onEvent: (ListPerformancesEvent) -> Unit,
) {
    when {
        state.isLoading -> LoadingIndicator()

        state.error != null -> ErrorSnackBar(errorTextFor(state.error), extraColors)

        else -> {
            PerformanceList(
                state = state,
                extraColors = extraColors,
                onEvent = onEvent
            )
        }
    }
}

@Composable
@Preview
fun ListPerformancesScreenPreview() {
    ListPerformancesScreen(ListPerformancesUiState(), {}, {})
}

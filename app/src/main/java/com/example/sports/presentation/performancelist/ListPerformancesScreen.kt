package com.example.sports.presentation.performancelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sports.R
import com.example.sports.domain.model.FilterType
import com.example.sports.domain.model.StorageType
import com.example.sports.presentation.insertperformance.InsertPerformanceEvent

@Composable
fun ListPerformancesScreen(
    uiState: ListPerformancesUiState,
    onEvent: (ListPerformancesEvent) -> Unit,
    onNavigateToInsert: () -> Unit,
) {
    val state = uiState

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToInsert) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Add"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Filter", style = MaterialTheme.typography.headlineMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(FilterType.ALL, FilterType.LOCAL, FilterType.REMOTE).forEach { type ->
                    FilterChip(
                        selected = state.selectedFilter == type,
                        onClick = {
                            onEvent(ListPerformancesEvent.FilterChanged(type))
                        },
                        label = { Text(type.name) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }

                state.errorMessage != null -> {
                    Text(state.errorMessage, color = Color.Red)
                }

                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.performances) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        when (item.storageType) {
                                            StorageType.LOCAL -> Color(0xFFE3F2FD)
                                            StorageType.REMOTE -> Color(0xFFFFF3E0)
                                        }
                                    ),
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(item.name, fontWeight = FontWeight.Bold)
                                    Text("Venue: ${item.venue}")
                                    Text("Duration: ${item.durationMinutes} min")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ListPerformancesScreenPreview() {
    ListPerformancesScreen(ListPerformancesUiState(), {}, {})
}

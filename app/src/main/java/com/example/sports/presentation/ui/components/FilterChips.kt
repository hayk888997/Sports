package com.example.sports.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.sports.domain.model.FilterType

@Composable
fun FilterChips(
    selected: FilterType,
    onFilterSelected: (FilterType) -> Unit
) {
    Text("Filter", style = MaterialTheme.typography.headlineMedium)

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(FilterType.ALL, FilterType.LOCAL, FilterType.REMOTE).forEach { type ->
            FilterChip(
                selected = selected == type,
                onClick = { onFilterSelected(type) },
                label = { Text(type.name) }
            )
        }
    }
}

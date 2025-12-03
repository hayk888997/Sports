package com.example.sports.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sports.domain.model.SportPerformance
import com.example.sports.presentation.ui.theme.ExtraColors

@Composable
fun PerformanceList(
    items: List<SportPerformance>,
    extraColors: ExtraColors,
    onRefresh: () -> Unit
) {
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = onRefresh,
        modifier = Modifier.padding(8.dp)
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(
                items = items,
                key = { it.id }
            ) { item ->
                PerformanceCard(item, extraColors)
            }
        }
    }
}

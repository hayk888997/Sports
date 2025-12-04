package com.example.sports.presentation.performancelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sports.domain.model.FilterType
import com.example.sports.presentation.commmon.components.PerformanceCard
import com.example.sports.presentation.ui.theme.ExtraColors

@Composable
fun PerformanceList(
    state: ListPerformancesUiState,
    extraColors: ExtraColors,
    onEvent: (ListPerformancesEvent) -> Unit,
) {

    val listState = rememberLazyListState()

    LaunchedEffect(state.selectedFilter) {
        val saved = when (state.selectedFilter) {
            FilterType.ALL -> state.scrollState.all
            FilterType.LOCAL -> state.scrollState.local
            FilterType.REMOTE -> state.scrollState.remote
        }

        listState.scrollToItem(saved)
    }

    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = { onEvent(ListPerformancesEvent.Refresh) },
        modifier = Modifier
            .padding(8.dp)
    ) {
        LaunchedEffect(listState.firstVisibleItemIndex) {
            onEvent(ListPerformancesEvent.ScrollPositionChanged(listState.firstVisibleItemIndex))
        }

        val items = when (state.selectedFilter) {
            FilterType.ALL -> state.allPerformances
            FilterType.LOCAL -> state.localPerformances
            FilterType.REMOTE -> state.remotePerformances
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = items,
                key = { it.id }
            ) { item ->
                PerformanceCard(item, extraColors)
            }
        }
    }
}

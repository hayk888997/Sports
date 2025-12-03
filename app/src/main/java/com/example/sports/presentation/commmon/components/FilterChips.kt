package com.example.sports.presentation.commmon.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sports.R
import com.example.sports.domain.model.FilterType

@Composable
fun FilterChips(
    selected: FilterType,
    onFilterSelected: (FilterType) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Column(
            modifier = Modifier
                .width(100.dp)
                .wrapContentHeight()
                .then(
                    Modifier.verticalScroll(rememberScrollState())
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                stringResource(R.string.filter_chip_title),
                style = MaterialTheme.typography.headlineMedium
            )

            listOf(FilterType.ALL, FilterType.LOCAL, FilterType.REMOTE).forEach { type ->
                FilterChip(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    selected = selected == type,
                    onClick = { onFilterSelected(type) },
                    label = { Text(type.name) }
                )
            }
        }
    } else {
        Text(
            stringResource(R.string.filter_chip_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(FilterType.ALL, FilterType.LOCAL, FilterType.REMOTE).forEach { type ->
                FilterChip(
                    modifier = Modifier
                        .wrapContentHeight()
                        .weight(1f),
                    selected = selected == type,
                    onClick = { onFilterSelected(type) },
                    label = { Text(type.toDisplayName()) }
                )
            }
        }
    }
}

@Composable
fun FilterType.toDisplayName(): String {
    return when (this) {
        FilterType.ALL -> stringResource(R.string.filter_all)
        FilterType.LOCAL -> stringResource(R.string.filter_local)
        FilterType.REMOTE -> stringResource(R.string.filter_remote)
    }
}

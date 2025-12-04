package com.example.sports.presentation.commmon.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sports.R
import com.example.sports.domain.model.StorageType
import com.example.sports.presentation.insertperformance.RadioButtonWithLabel

@Composable
fun StorageSelector(
    selected: StorageType,
    onSelect: (StorageType) -> Unit
) {
    Column {
        Text(text = stringResource(R.string.insert_performance_selection_storage_title))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            RadioButtonWithLabel(
                selected = selected == StorageType.LOCAL,
                onClick = { onSelect(StorageType.LOCAL) },
                label = stringResource(R.string.insert_performance_selection_storage_local)
            )

            RadioButtonWithLabel(
                selected = selected == StorageType.REMOTE,
                onClick = { onSelect(StorageType.REMOTE) },
                label = stringResource(R.string.insert_performance_selection_storage_remote)
            )
        }
    }
}

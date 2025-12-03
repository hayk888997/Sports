package com.example.sports.presentation.insertperformance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.sports.R
import com.example.sports.presentation.ui.components.ErrorSnackBar
import com.example.sports.presentation.ui.components.LoadingIndicator
import com.example.sports.presentation.ui.components.PerformanceInputField
import com.example.sports.presentation.ui.components.StorageSelector
import com.example.sports.presentation.ui.theme.LocalExtraColors

@Composable
fun InsertPerformanceScreen(
    uiState: InsertPerformanceUiState,
    onEvent: (InsertPerformanceEvent) -> Unit,
    onSaved: () -> Unit
) {
    val state = uiState
    val scrollState = rememberScrollState()
    val extraColors = LocalExtraColors.current

    LaunchedEffect(state.success) {
        if (state.success) onSaved()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(extraColors.screenBg)
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            stringResource(R.string.insert_performance_title),
            style = MaterialTheme.typography.headlineMedium
        )

        PerformanceInputField(
            value = state.name,
            onValueChange = { onEvent(InsertPerformanceEvent.NameChanged(it)) },
            label = stringResource(R.string.insert_performance_field_name)
        )

        PerformanceInputField(
            value = state.venue,
            onValueChange = { onEvent(InsertPerformanceEvent.VenueChanged(it)) },
            label = stringResource(R.string.insert_performance_field_venue)
        )

        PerformanceInputField(
            value = state.duration,
            onValueChange = { onEvent(InsertPerformanceEvent.DurationChanged(it)) },
            label = stringResource(R.string.insert_performance_field_duration),
            keyboardType = KeyboardType.Number
        )

        StorageSelector(
            selected = state.storageType,
            onSelect = { onEvent(InsertPerformanceEvent.StorageTypeChanged(it)) }
        )

        Button(
            onClick = { onEvent(InsertPerformanceEvent.Submit) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
        ) {
            Text(stringResource(R.string.insert_performance_btn_save))
        }

        if (state.isLoading) {
            LoadingIndicator()
        }
        if (!state.errorMessage.isNullOrEmpty()) {
            ErrorSnackBar(state.errorMessage, extraColors)
        }

    }
}

@Composable
fun RadioButtonWithLabel(selected: Boolean, onClick: () -> Unit, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = onClick)
        Text(label)
    }
}

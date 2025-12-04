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
import com.example.sports.presentation.commmon.components.ErrorSnackBar
import com.example.sports.presentation.commmon.components.LoadingIndicator
import com.example.sports.presentation.commmon.components.PerformanceInputField
import com.example.sports.presentation.commmon.components.StorageSelector
import com.example.sports.presentation.commmon.errorTextFor
import com.example.sports.presentation.commmon.isLandscape
import com.example.sports.presentation.ui.theme.ExtraColors
import com.example.sports.presentation.ui.theme.LocalExtraColors

@Composable
fun InsertPerformanceScreen(
    uiState: InsertPerformanceUiState,
    onEvent: (InsertPerformanceEvent) -> Unit,
    onSaved: () -> Unit
) {
    val extraColors = LocalExtraColors.current

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            onEvent(InsertPerformanceEvent.OnSuccessHandled)
            onSaved()
        }
    }

    if (isLandscape()) {
        LandscapeInsertLayout(uiState, extraColors, onEvent)
    } else {
        PortraitInsertLayout(uiState, extraColors, onEvent)
    }
}

@Composable
private fun PortraitInsertLayout(
    uiState: InsertPerformanceUiState,
    extraColors: ExtraColors,
    onEvent: (InsertPerformanceEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(extraColors.screenBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        InputFieldsColumn(uiState, onEvent)
        ActionsColumn(uiState, extraColors, onEvent)
    }
}

@Composable
private fun LandscapeInsertLayout(
    uiState: InsertPerformanceUiState,
    extraColors: ExtraColors,
    onEvent: (InsertPerformanceEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(extraColors.screenBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            InputFieldsColumn(uiState, onEvent)
        }
        Column(modifier = Modifier.weight(1f)) {
            ActionsColumn(uiState, extraColors, onEvent)
        }
    }
}

@Composable
private fun InputFieldsColumn(
    uiState: InsertPerformanceUiState,
    onEvent: (InsertPerformanceEvent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(
            stringResource(R.string.insert_performance_title),
            style = MaterialTheme.typography.headlineMedium
        )

        PerformanceInputField(
            value = uiState.name,
            onValueChange = { onEvent(InsertPerformanceEvent.NameChanged(it)) },
            label = stringResource(R.string.insert_performance_field_name)
        )

        PerformanceInputField(
            value = uiState.venue,
            onValueChange = { onEvent(InsertPerformanceEvent.VenueChanged(it)) },
            label = stringResource(R.string.insert_performance_field_venue)
        )

        PerformanceInputField(
            value = uiState.duration,
            onValueChange = { onEvent(InsertPerformanceEvent.DurationChanged(it)) },
            label = stringResource(R.string.insert_performance_field_duration),
            keyboardType = KeyboardType.Number
        )
    }
}

@Composable
private fun ActionsColumn(
    uiState: InsertPerformanceUiState,
    extraColors: ExtraColors,
    onEvent: (InsertPerformanceEvent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        StorageSelector(
            selected = uiState.storageType,
            onSelect = { onEvent(InsertPerformanceEvent.StorageTypeChanged(it)) }
        )

        Button(
            onClick = { onEvent(InsertPerformanceEvent.Submit) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading,
        ) {
            Text(stringResource(R.string.insert_performance_btn_save))
        }

        if (uiState.isLoading) {
            LoadingIndicator()
        }

        if (uiState.error != null) {
            ErrorSnackBar(errorTextFor(uiState.error), extraColors)
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

package com.example.sports.presentation.insertperformance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.sports.domain.model.StorageType

@Composable
fun InsertPerformanceScreen(
    uiState: InsertPerformanceUiState,
    onEvent: (InsertPerformanceEvent) -> Unit,
    onSaved: () -> Unit
) {
    val state = uiState

    LaunchedEffect(state.success) {
        if (state.success) {
            onSaved()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Add Performance", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = state.name,
            onValueChange = { onEvent(InsertPerformanceEvent.NameChanged(it)) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.venue,
            onValueChange = { onEvent(InsertPerformanceEvent.VenueChanged(it)) },
            label = { Text("Venue") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.duration,
            onValueChange = { onEvent(InsertPerformanceEvent.DurationChanged(it)) },
            label = { Text("Duration (min)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Text("Save to:")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            RadioButtonWithLabel(
                selected = state.storageType == StorageType.LOCAL,
                onClick = { onEvent(InsertPerformanceEvent.StorageTypeChanged(StorageType.LOCAL)) },
                label = "Local"
            )
            RadioButtonWithLabel(
                selected = state.storageType == StorageType.REMOTE,
                onClick = { onEvent(InsertPerformanceEvent.StorageTypeChanged(StorageType.REMOTE)) },
                label = "Remote"
            )
        }

        Button(
            onClick = { onEvent(InsertPerformanceEvent.Submit) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            Text("Save")
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        state.errorMessage?.let {
            Snackbar(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(it)
            }
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

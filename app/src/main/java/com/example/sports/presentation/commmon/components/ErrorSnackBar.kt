package com.example.sports.presentation.commmon.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.sports.presentation.ui.theme.ExtraColors

@Composable
fun ErrorSnackBar(message: String, extraColors: ExtraColors) {
    Text(message, color = extraColors.error)
}

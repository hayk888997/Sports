package com.example.sports.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sports.R
import com.example.sports.domain.model.SportPerformance
import com.example.sports.domain.model.StorageType
import com.example.sports.presentation.ui.theme.ExtraColors

@Composable
fun PerformanceCard(
    item: SportPerformance,
    extraColors: ExtraColors
) {
    val bgColor = when (item.storageType) {
        StorageType.LOCAL -> extraColors.localBg
        StorageType.REMOTE -> extraColors.remoteBg
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(item.name, fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.performance_venue, item.venue))
        Text(stringResource(R.string.performance_duration, item.durationMinutes))
    }
}

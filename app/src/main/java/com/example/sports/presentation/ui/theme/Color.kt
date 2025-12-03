package com.example.sports.presentation.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

data class ExtraColors(
    val screenBg: Color,
    val localBg: Color,
    val remoteBg: Color,
    val error: Color
)

val LocalExtraColors = staticCompositionLocalOf {
    ExtraColors(
        screenBg = Color.Unspecified,
        localBg = Color.Unspecified,
        remoteBg = Color.Unspecified,
        error = Color.Unspecified
    )
}

val ScreenBgLight = Color(0xFFF4F1EC)
val LocalBgLight = Color(0xFF2F5B8A)   // light blue
val RemoteBgLight = Color(0xFFF6C744)  // light orange

val ScreenBgDark = Color(0xFFF4F1EC)
val LocalBgDark = Color(0xFF4C7BAA)    // darker blue variant
val RemoteBgDark = Color(0xFFF7DA72)   // darker orange variant

val ErrorRed = Color(0xFFE04B4A)

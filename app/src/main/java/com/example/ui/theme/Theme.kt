package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PhonePeDarkColorScheme = darkColorScheme(
    primary = PhonePePurple,
    onPrimary = Color.White,
    primaryContainer = PhonePePurpleContainer,
    onPrimaryContainer = Color(0xFFE9D5FF),
    secondary = PhonePePurpleLight,
    onSecondary = Color.White,
    secondaryContainer = PhonePePurpleTile,
    onSecondaryContainer = Color(0xFFDDD6FE),
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceBorder,
    error = AccentRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PhonePeDarkColorScheme,
        typography = Typography,
        content = content
    )
}

package com.example.kleenpride.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Colors
val BlackBackground = Color(0xFF000000)
val LimeGreen = Color(0xFF00FF00)
val WhiteText = Color(0xFFFFFFFF)

// Dark theme scheme
private val DarkColorScheme = darkColorScheme(
    primary = LimeGreen, // Buttons
    secondary = LimeGreen,
    background = BlackBackground,
    surface = WhiteText, // For text fields background
    onPrimary = BlackBackground, // Text on buttons
    onSecondary = BlackBackground,
    onBackground = WhiteText,
    onSurface = BlackBackground
)

// Light theme (you can use same as dark if only black background is needed)
private val LightColorScheme = lightColorScheme(
    primary = LimeGreen,
    secondary = LimeGreen,
    background = BlackBackground,
    surface = WhiteText,
    onPrimary = BlackBackground,
    onSecondary = BlackBackground,
    onBackground = WhiteText,
    onSurface = BlackBackground
)

@Composable
fun KleenPrideTheme(
    darkTheme: Boolean = true, // Force dark mode for your black background
    dynamicColor: Boolean = false, // Disable dynamic colors
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

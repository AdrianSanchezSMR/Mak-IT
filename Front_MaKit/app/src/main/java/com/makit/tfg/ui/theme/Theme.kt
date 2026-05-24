package com.makit.tfg.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = MakGreen,
    onPrimary = MakSurface,
    primaryContainer = MakMint,
    onPrimaryContainer = MakGreenDark,
    secondary = MakGreenLight,
    onSecondary = MakSurface,
    background = MakSurface,
    onBackground = MakOnSurface,
    surface = MakSurface,
    onSurface = MakOnSurface,
    surfaceVariant = MakMint,
    onSurfaceVariant = MakOnSurfaceMuted,
    outline = MakCardBorder
)

@Composable
fun MakITTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = MakSurface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

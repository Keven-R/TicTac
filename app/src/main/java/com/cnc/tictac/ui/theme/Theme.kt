package com.cnc.tictac.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Both DarkColorScheme and LightColorScheme have the same colours for now.
// TODO: if there is time, add LightColorScheme
private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkInverse,
)

private val LightColors = lightColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkInverse,
)

@Composable
fun TicTacTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    // App colour scheme and typography
    MaterialTheme(
        colorScheme = colors,
        content = content,
        typography = Typography,
    )
}
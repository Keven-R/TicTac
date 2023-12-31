package com.cnc.tictac.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// Both DarkColorScheme and LightColorScheme have the same colours for now.
// TODO: if there is time, add LightColorScheme
private val DarkColors = darkColorScheme(
    primary = DarkNeutral100,
    onPrimary = DarkNeutral00,
    secondary = DarkNeutral00,
    onSecondary = DarkNeutral100,
    outline = DarkNeutral00.copy(alpha = 0.16f),
    outlineVariant = DarkNeutral00.copy(alpha = 0.4f)
)

private val LightColors = lightColorScheme(
    primary = DarkNeutral00,
    onPrimary = DarkNeutral100,
    secondary = DarkNeutral100,
    onSecondary = DarkNeutral00,
    outline = DarkNeutral100.copy(alpha = 0.32f),
    outlineVariant = DarkNeutral100.copy(alpha = 0.64f)
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

    // Switch colours depending on system status
    val systemUiController = rememberSystemUiController()
    val mainColor = if (!useDarkTheme) DarkColors.primary else LightColors.primary
    val onMainColor = if (!useDarkTheme) DarkColors.onPrimary else LightColors.onPrimary
    systemUiController.setSystemBarsColor(mainColor)
    systemUiController.setNavigationBarColor(onMainColor)
    systemUiController.setStatusBarColor(onMainColor)
}
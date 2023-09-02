package com.cnc.tictac.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.unit.sp
import com.cnc.tictac.R

// Initialize the GoogleFont.Provider with the credentials for Google Fonts
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// Use Inter font
val Inter = FontFamily(
    Font(
        googleFont = GoogleFont("Inter"),
        fontProvider = provider,
    )
)

// Set of Material typography styles to start with
val Typography = Typography(
    /* USES
     *
     * - App logo on HomeScreen.kt
     */
    displaySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight(700),
        fontSize = 24.sp
    ),

    /* USES
     *
     * - Buttons on HomeScreen.kt
     */
    headlineLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight(700),
        fontSize = 48.sp
    ),

    /* USES
     *
     * - Buttons on HomeScreen.kt
     */
    labelSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight(700),
        fontSize = 16.sp
    ),
)
package com.example.wanderer.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Typography

private val DarkColorScheme = darkColorScheme(
    primary = GreenMain,
    onPrimary = Color.Black,
    primaryContainer = GreenDark,
    onPrimaryContainer = WhiteMain,
    secondary = BlueGrey,
    onSecondary = WhiteMain,
    background = BlueBackground,
    onBackground = WhiteMain,
    surface = BlueBackground,
    onSurface = WhiteMain,
    surfaceVariant = BlueGrey,
    onSurfaceVariant = WhiteMain
)


// We'll keep a version of LightColorScheme just in case,
// but we'll default the theme to use DarkColorScheme.
private val LightColorScheme = lightColorScheme(
    primary = GreenMain,
    onPrimary = Color.Black,
    primaryContainer = GreenDark,
    onPrimaryContainer = WhiteMain,
    background = WhiteMain,
    onBackground = Color.Black,
    surface = WhiteMain,
    onSurface = Color.Black
)

@Composable
fun WandererTheme(
    // Force dark theme by default as requested

    darkTheme: Boolean = true,
    // Set to false to prioritize your custom colors over Android's dynamic colors
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )




}

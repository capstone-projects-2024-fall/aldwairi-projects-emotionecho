package com.temple.aldwairi_projects_emotionecho.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkTempleColorScheme = darkColorScheme(
    primary = TempleCherryRed80,
    onPrimary = TempleCherryRed37,
    primaryContainer = TempleCherryRed20,
    onPrimaryContainer = TempleYellow90,
    inversePrimary = TempleCherryRed50,
    secondary = TempleYellow80,
    onSecondary = TempleYellow30,
    secondaryContainer = TempleYellow20,
    onSecondaryContainer = TempleYellow90,
    tertiary = TempleMetallicSilver85,
    onTertiary = TempleMetallicSilver35,
    tertiaryContainer = TempleMetallicSilver25,
    onTertiaryContainer = TempleMetallicSilver95,
    surface = TempleCherryRed10,
    outline = TempleCherryRed37,
    outlineVariant = TempleCherryRed20
)
private val LightTempleColorScheme = lightColorScheme(
    primary = TempleCherryRed37,
    onPrimary = Color.White,
    primaryContainer = TempleCherryRed80,
    onPrimaryContainer = TempleYellow10,
    inversePrimary = TempleCherryRed70,
    secondary = TempleYellow40,
    onSecondary = Color.White,
    secondaryContainer = TempleYellow90,
    onSecondaryContainer = TempleYellow10,
    tertiary = TempleMetallicSilver45,
    onTertiary = Color.White,
    tertiaryContainer = TempleMetallicSilver95,
    onTertiaryContainer = TempleMetallicSilver15,
    surface = TempleCherryRed90,
    outline = TempleCherryRed60,
    outlineVariant = TempleCherryRed70
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun AldwairiprojectsemotionechoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkTempleColorScheme
        else -> LightTempleColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.secondary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
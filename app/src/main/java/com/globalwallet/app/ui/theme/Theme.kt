package com.globalwallet.app.ui.theme

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

/**
 * Global Wallet 3D Liquid Glass Theme
 * 
 * Features:
 * - Glassmorphism effects with translucent backgrounds
 * - Neon blue/purple gradients for futuristic feel
 * - Dynamic color schemes for immersive experience
 * - Dark/light theme support with liquid transitions
 */

// Primary neon colors for 3D Liquid Glass UI
private val NeonBlue = Color(0xFF00D4FF)
private val NeonPurple = Color(0xFF8B5CF6)
private val NeonPink = Color(0xFFEC4899)
private val GlassBackground = Color(0x1AFFFFFF)
private val GlassSurface = Color(0x0DFFFFFF)
private val GlassBorder = Color(0x33FFFFFF)

// Dark theme colors
private val DarkNeonBlue = Color(0xFF00B8E6)
private val DarkNeonPurple = Color(0xFF7C3AED)
private val DarkNeonPink = Color(0xFFDB2777)
private val DarkGlassBackground = Color(0x0A000000)
private val DarkGlassSurface = Color(0x0F000000)
private val DarkGlassBorder = Color(0x33000000)

private val LightColorScheme = lightColorScheme(
    primary = NeonBlue,
    onPrimary = Color.White,
    primaryContainer = NeonPurple.copy(alpha = 0.1f),
    onPrimaryContainer = NeonPurple,
    secondary = NeonPurple,
    onSecondary = Color.White,
    secondaryContainer = NeonPink.copy(alpha = 0.1f),
    onSecondaryContainer = NeonPink,
    tertiary = NeonPink,
    onTertiary = Color.White,
    tertiaryContainer = NeonBlue.copy(alpha = 0.1f),
    onTertiaryContainer = NeonBlue,
    error = Color(0xFFEF4444),
    onError = Color.White,
    errorContainer = Color(0xFFEF4444).copy(alpha = 0.1f),
    onErrorContainer = Color(0xFFEF4444),
    background = Color(0xFF0A0A0A),
    onBackground = Color.White,
    surface = GlassSurface,
    onSurface = Color.White,
    surfaceVariant = GlassBackground,
    onSurfaceVariant = Color.White.copy(alpha = 0.8f),
    outline = GlassBorder,
    outlineVariant = GlassBorder.copy(alpha = 0.5f),
    scrim = Color.Black.copy(alpha = 0.32f),
    inverseSurface = Color.White,
    inverseOnSurface = Color.Black,
    inversePrimary = NeonBlue,
    surfaceDim = Color(0xFF0A0A0A),
    surfaceBright = Color(0xFF1A1A1A),
    surfaceContainerLowest = Color(0xFF050505),
    surfaceContainerLow = Color(0xFF0F0F0F),
    surfaceContainer = Color(0xFF141414),
    surfaceContainerHigh = Color(0xFF1E1E1E),
    surfaceContainerHighest = Color(0xFF292929),
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkNeonBlue,
    onPrimary = Color.Black,
    primaryContainer = DarkNeonPurple.copy(alpha = 0.2f),
    onPrimaryContainer = DarkNeonPurple,
    secondary = DarkNeonPurple,
    onSecondary = Color.Black,
    secondaryContainer = DarkNeonPink.copy(alpha = 0.2f),
    onSecondaryContainer = DarkNeonPink,
    tertiary = DarkNeonPink,
    onTertiary = Color.Black,
    tertiaryContainer = DarkNeonBlue.copy(alpha = 0.2f),
    onTertiaryContainer = DarkNeonBlue,
    error = Color(0xFFF87171),
    onError = Color.Black,
    errorContainer = Color(0xFFF87171).copy(alpha = 0.2f),
    onErrorContainer = Color(0xFFF87171),
    background = Color(0xFF000000),
    onBackground = Color.White,
    surface = DarkGlassSurface,
    onSurface = Color.White,
    surfaceVariant = DarkGlassBackground,
    onSurfaceVariant = Color.White.copy(alpha = 0.7f),
    outline = DarkGlassBorder,
    outlineVariant = DarkGlassBorder.copy(alpha = 0.5f),
    scrim = Color.Black.copy(alpha = 0.4f),
    inverseSurface = Color.White,
    inverseOnSurface = Color.Black,
    inversePrimary = DarkNeonBlue,
    surfaceDim = Color(0xFF000000),
    surfaceBright = Color(0xFF0A0A0A),
    surfaceContainerLowest = Color(0xFF000000),
    surfaceContainerLow = Color(0xFF050505),
    surfaceContainer = Color(0xFF0A0A0A),
    surfaceContainerHigh = Color(0xFF0F0F0F),
    surfaceContainerHighest = Color(0xFF141414),
)

@Composable
fun GlobalWalletTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
} 
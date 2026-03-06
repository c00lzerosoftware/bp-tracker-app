package com.bptracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

/**
 * Blood Pressure Tracker - Material 3 Theme
 *
 * Implements the complete color scheme with support for Material You dynamic theming on Android 12+
 */

// Light theme color palette
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0D7377),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFA8E6E9),
    onPrimaryContainer = Color(0xFF00363A),

    secondary = Color(0xFF52B788),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD8F3DC),
    onSecondaryContainer = Color(0xFF0A3818),

    tertiary = Color(0xFF4A5899),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD9E2FF),
    onTertiaryContainer = Color(0xFF0E1C52),

    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFF410002),

    background = Color(0xFFFAFCFC),
    onBackground = Color(0xFF191C1D),
    surface = Color(0xFFFAFCFC),
    onSurface = Color(0xFF191C1D),
    surfaceVariant = Color(0xFFDBE4E6),
    onSurfaceVariant = Color(0xFF3F484A),

    outline = Color(0xFF6F797B),
    inverseOnSurface = Color(0xFFEFF1F1),
    inverseSurface = Color(0xFF2E3132),
    inversePrimary = Color(0xFF87D1D5),
    surfaceTint = Color(0xFF0D7377),
    outlineVariant = Color(0xFFBFC8CA),
    scrim = Color(0xFF000000),
)

// Dark theme color palette
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF87D1D5),
    onPrimary = Color(0xFF003739),
    primaryContainer = Color(0xFF004F52),
    onPrimaryContainer = Color(0xFFA8E6E9),

    secondary = Color(0xFF95D5B2),
    onSecondary = Color(0xFF00522A),
    secondaryContainer = Color(0xFF007239),
    onSecondaryContainer = Color(0xFFD8F3DC),

    tertiary = Color(0xFFB1C5FF),
    onTertiary = Color(0xFF1B2E6B),
    tertiaryContainer = Color(0xFF334081),
    onTertiaryContainer = Color(0xFFD9E2FF),

    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),

    background = Color(0xFF0F1415),
    onBackground = Color(0xFFE1E4E5),
    surface = Color(0xFF0F1415),
    onSurface = Color(0xFFE1E4E5),
    surfaceVariant = Color(0xFF3F484A),
    onSurfaceVariant = Color(0xFFBFC8CA),

    outline = Color(0xFF899294),
    inverseOnSurface = Color(0xFF2E3132),
    inverseSurface = Color(0xFFE1E4E5),
    inversePrimary = Color(0xFF0D7377),
    surfaceTint = Color(0xFF87D1D5),
    outlineVariant = Color(0xFF3F484A),
    scrim = Color(0xFF000000),
)

/**
 * Health status colors - consistent across themes
 * Used for BP range indicators
 */
object HealthColors {
    val normal = Color(0xFF52B788)
    val normalDark = Color(0xFF95D5B2)
    val elevated = Color(0xFFFFB700)
    val elevatedDark = Color(0xFFFFD666)
    val highStage1 = Color(0xFFF77F00)
    val highStage1Dark = Color(0xFFFFB366)
    val highStage2 = Color(0xFFDC2F02)
    val highStage2Dark = Color(0xFFFF8A80)
    val crisis = Color(0xFF9D0208)
    val crisisDark = Color(0xFFFF5252)
    val low = Color(0xFF4A5899)
    val lowDark = Color(0xFFB1C5FF)

    @Composable
    fun getStatusColor(status: String, isDark: Boolean = isSystemInDarkTheme()): Color {
        return when (status.uppercase()) {
            "NORMAL" -> if (isDark) normalDark else normal
            "ELEVATED" -> if (isDark) elevatedDark else elevated
            "HIGH_STAGE_1" -> if (isDark) highStage1Dark else highStage1
            "HIGH_STAGE_2" -> if (isDark) highStage2Dark else highStage2
            "CRISIS" -> if (isDark) crisisDark else crisis
            "LOW" -> if (isDark) lowDark else low
            else -> if (isDark) normalDark else normal
        }
    }
}

/**
 * Main theme composable with Material You support
 */
@Composable
fun BPTrackerTheme(
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
        typography = BPTypography,
        shapes = BPShapes,
        content = content
    )
}

/**
 * Typography scale following Material 3
 */
val BPTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 57.sp,
        lineHeight = 64.sp,
        fontWeight = FontWeight.Normal
    ),
    headlineMedium = TextStyle(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Normal
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium
    )
)

/**
 * Shape definitions
 */
val BPShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

/**
 * Custom dimensions and spacing
 */
object Dimensions {
    val spaceSmall = 8.dp
    val spaceMedium = 16.dp
    val spaceLarge = 24.dp
    val spaceXLarge = 32.dp
    val minTouchTarget = 48.dp
    val cardMinHeight = 96.dp
    val chartHeight = 240.dp
}

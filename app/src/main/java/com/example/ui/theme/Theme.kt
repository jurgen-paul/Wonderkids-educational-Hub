package com.example.ui.theme

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

private val DarkColorScheme =
  darkColorScheme(
    primary = NaturalSageLight,
    onPrimary = NaturalForestDark,
    primaryContainer = NaturalForest,
    onPrimaryContainer = NaturalSageContainer,
    secondary = NaturalOchreLight,
    onSecondary = Color(0xFF261904),
    tertiary = NaturalTerracottaLight,
    background = Color(0xFF191C19),
    surface = Color(0xFF222622),
    onBackground = Color(0xFFE1E3DE),
    onSurface = Color(0xFFE1E3DE),
    surfaceVariant = Color(0xFF333832),
    outline = Color(0xFF8A9388),
    error = ErrorColor
  )

private val LightColorScheme =
  lightColorScheme(
    primary = PrimaryKids,
    onPrimary = OnPrimaryKids,
    primaryContainer = PrimaryContainerKids,
    onPrimaryContainer = NaturalTextPrimary,
    secondary = SecondaryKids,
    onSecondary = Color.White,
    secondaryContainer = NaturalSageContainer,
    onSecondaryContainer = NaturalTextPrimary,
    tertiary = TertiaryKids,
    onTertiary = Color.White,
    tertiaryContainer = NaturalOchreLight,
    onTertiaryContainer = NaturalTextPrimary,
    background = BackgroundKids,
    surface = SurfaceKids,
    onBackground = NaturalTextPrimary,
    onSurface = NaturalTextPrimary,
    surfaceVariant = NaturalSurfaceVariant,
    outline = NaturalBorder,
    error = ErrorColor
  )

@Composable
fun WonderKidsTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) = WonderKidsTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)


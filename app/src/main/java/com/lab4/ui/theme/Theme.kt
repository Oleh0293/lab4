package com.lab4.ui.theme

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1E88E5),  // Синій для заголовків
    secondary = Color(0xFF42A5F5),  // Світло-синій акцент
    tertiary = Color(0xFF90CAF9),  // Пастельний блакитний
    background = Color(0xFF121212),  // Темно-сірий фон
    surface = Color(0xFF1E1E1E),  // Майже чорний для карток
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color(0xFFB3E5FC),  // Світлий блакитний для тексту
    onBackground = Color(0xFFE0E0E0),  // Світло-сірий для тексту
    onSurface = Color(0xFFE0E0E0),  // Світло-сірий текст на темній поверхні
    primaryContainer = Color(0xFF3949AB),  // Темно-синій для кнопок
    onPrimaryContainer = Color.White,  // Білий текст на кнопках
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1976D2),  // Глибокий синій
    secondary = Color(0xFFBBDEFB),  // Пастельно-блакитний
    tertiary = Color(0xFF64B5F6),  // Світло-блакитний акцент
    background = Color(0xFFF5F5F5),  // Світло-сірий фон
    surface = Color.White,  // Білий для карток
    onPrimary = Color.White,
    onSecondary = Color(0xFF0D47A1),  // Темно-синій текст
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    primaryContainer = Color(0xFF90CAF9),  // Світло-блакитний для кнопок
    onPrimaryContainer = Color.Black,  // Чорний текст на кнопках
)

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun Lab4Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

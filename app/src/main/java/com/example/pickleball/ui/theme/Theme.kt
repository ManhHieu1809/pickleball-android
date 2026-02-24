package com.example.pickleball.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun PickleballTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if(darkTheme){
        darkColorScheme(
            primary = PrimaryGreen,
            background = BackgroundDark,
            onBackground = Color.White
        )
    } else{
        lightColorScheme(
            primary = PrimaryGreen,
            background = BackgroundLight,
            onBackground = NavyDeep
        )
    }

    MaterialTheme(
            colorScheme = colors,
            typography = AppTypography,
            content = content
    )
}
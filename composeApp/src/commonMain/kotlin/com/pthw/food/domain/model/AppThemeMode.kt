package com.pthw.food.domain.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource

/**
 * Created by P.T.H.W on 23/07/2024.
 */
data class AppThemeMode(
    val title: StringResource,
    val themeCode: String
) {
    companion object {
        const val LIGHT_MODE = "light_mode"
        const val DARK_MODE = "dark_mode"
        const val SYSTEM_DEFAULT = "system_default"

        @Composable
        fun isDarkMode(themeCode: String) = when (themeCode) {
            LIGHT_MODE -> false
            DARK_MODE -> true
            else -> isSystemInDarkTheme()
        }
    }
}

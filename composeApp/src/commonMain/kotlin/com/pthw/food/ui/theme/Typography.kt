package com.pthw.food.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import fooddimultiplatform.composeapp.generated.resources.*
import org.jetbrains.compose.resources.Font

/**
 * Created by P.T.H.W on 24/01/2024.
 */


@Composable
fun Typography.defaultFontFamily(): Typography {
    val fontFamily = FontFamily(
        Font(Res.font.poppins_thin, FontWeight.Thin),
        Font(Res.font.poppins_extra_light, FontWeight.ExtraLight),
        Font(Res.font.poppins_light, FontWeight.Light),
        Font(Res.font.poppins_regular, FontWeight.Normal),
        Font(Res.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),
        Font(Res.font.poppins_medium, FontWeight.Medium),
        Font(Res.font.poppins_semi_bold, FontWeight.SemiBold),
        Font(Res.font.poppins_extra_bold, FontWeight.ExtraBold),
        Font(Res.font.poppins_bold, FontWeight.Bold),
        Font(Res.font.poppins_black, FontWeight.Black)
    )


    return this.copy(
        displayLarge = this.displayLarge.copy(
            fontFamily = fontFamily,
        ),
        displayMedium = this.displayMedium.copy(
            fontFamily = fontFamily,
        ),
        displaySmall = this.displaySmall.copy(
            fontFamily = fontFamily,
        ),
        headlineLarge = this.headlineLarge.copy(
            fontFamily = fontFamily,
        ),
        headlineMedium = this.headlineMedium.copy(
            fontFamily = fontFamily,
        ),
        headlineSmall = this.headlineSmall.copy(
            fontFamily = fontFamily,
        ),
        titleLarge = this.titleLarge.copy(
            fontFamily = fontFamily,
        ),
        titleMedium = this.titleMedium.copy(
            fontFamily = fontFamily,
        ),
        titleSmall = this.titleSmall.copy(
            fontFamily = fontFamily,
        ),
        bodyLarge = this.bodyLarge.copy(
            fontFamily = fontFamily,
        ),
        bodyMedium = this.bodyMedium.copy(
            fontFamily = fontFamily,
        ),
        bodySmall = this.bodySmall.copy(
            fontFamily = fontFamily,
        ),
        labelLarge = this.labelLarge.copy(
            fontFamily = fontFamily,
        ),
        labelMedium = this.labelMedium.copy(
            fontFamily = fontFamily,
        ),
        labelSmall = this.labelSmall.copy(
            fontFamily = fontFamily,
        )
    )
}

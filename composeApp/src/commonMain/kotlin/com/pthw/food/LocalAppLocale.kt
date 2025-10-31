package com.pthw.food

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue

/**
 * Created by phyothihawin on 29/10/2025.
 */

expect object LocalAppLocale {
    val current: String @Composable get
    @Composable infix fun provides(value: String?): ProvidedValue<*>
}
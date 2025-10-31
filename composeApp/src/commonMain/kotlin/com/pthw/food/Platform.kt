package com.pthw.food

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

enum class Orientation {
    Portrait, Landscape, Unknown
}

interface Platform {
    val name: String

    @Composable
    fun getScreenWidth(): Dp

    @Composable
    fun getScreenHeight(): Dp

    @Composable
    fun getCurrentOrientation(): Orientation

    @Composable
    fun setSystemBarTheme(darkTheme: Boolean)

    fun initKmpNotifier()
}


expect fun getPlatform(): Platform
package com.pthw.food.expects

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

enum class Orientation {
    Portrait, Landscape, Unknown
}

enum class PlatformType {
    Android, iOS
}

interface Platform {
    val type: PlatformType

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
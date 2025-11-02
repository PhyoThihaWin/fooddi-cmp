package com.pthw.food.expects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pthw.food.ui.theme.ColorPrimary
import com.pthw.food.ui.theme.md_theme_dark_background
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import platform.UIKit.*

class IOSPlatform(override val type: PlatformType = PlatformType.iOS) : Platform {

    @Composable
    override fun getScreenWidth(): Dp = LocalWindowInfo.current.containerSize.width.pxToPoint().dp

    @Composable
    override fun getScreenHeight(): Dp = LocalWindowInfo.current.containerSize.height.pxToPoint().dp

    @Composable
    override fun getCurrentOrientation(): Orientation {
        val windowScene = UIApplication.sharedApplication.connectedScenes.firstOrNull() as? UIWindowScene
        return when (windowScene?.interfaceOrientation) {
            UIInterfaceOrientationPortrait -> Orientation.Portrait
            UIInterfaceOrientationLandscapeLeft, UIInterfaceOrientationLandscapeRight -> Orientation.Landscape
            else -> Orientation.Unknown
        }
    }

    override fun initKmpNotifier() {
        NotifierManager.initialize(
            NotificationPlatformConfiguration.Ios(
                showPushNotification = true,
                askNotificationPermissionOnStart = false,
            )
        )
    }
}

fun Int.pxToPoint(): Double = this.toDouble() / UIScreen.mainScreen.scale

actual fun getPlatform(): Platform = IOSPlatform()
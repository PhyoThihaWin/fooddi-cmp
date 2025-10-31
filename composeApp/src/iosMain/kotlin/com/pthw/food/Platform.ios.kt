package com.pthw.food

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

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

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

    @Composable
    override fun setSystemBarTheme(darkTheme: Boolean) {
        val statusBar = statusBarView()
        SideEffect {
            if (darkTheme) {
                statusBar.backgroundColor = md_theme_dark_background.toUIColor()
            } else {
                statusBar.backgroundColor = ColorPrimary.toUIColor()
            }
//            UINavigationBar.appearance().backgroundColor = ColorPrimary.toUIColor()
        }
    }

    override fun initKmpNotifier() {
        NotifierManager.initialize(
            NotificationPlatformConfiguration.Ios(
                showPushNotification = true,
                askNotificationPermissionOnStart = true,
            )
        )
    }
}

fun Int.pxToPoint(): Double = this.toDouble() / UIScreen.mainScreen.scale

actual fun getPlatform(): Platform = IOSPlatform()

@OptIn(ExperimentalForeignApi::class)
@Composable
private fun statusBarView() = remember {
    val keyWindow: UIWindow? =
        UIApplication.sharedApplication.windows.firstOrNull { (it as? UIWindow)?.isKeyWindow() == true } as? UIWindow
    val safeAreaInsets = UIApplication.sharedApplication.keyWindow?.safeAreaInsets
    val width = UIScreen.mainScreen.bounds.useContents { this.size.width }
    var topInsets = 0.0

    safeAreaInsets?.let {
        topInsets = safeAreaInsets.useContents {
            this.top
        }
    }

    val tag = 3848245L

    val statusBarView = UIView(frame = CGRectMake(0.0, 0.0, width, topInsets))

    keyWindow?.viewWithTag(tag) ?: run {
        statusBarView.tag = tag
        statusBarView.layer.zPosition = 999999.0
        keyWindow?.addSubview(statusBarView)
        statusBarView
    }
}

private fun Color.toUIColor(): UIColor = UIColor(
    red = this.red.toDouble(),
    green = this.green.toDouble(),
    blue = this.blue.toDouble(),
    alpha = this.alpha.toDouble()
)
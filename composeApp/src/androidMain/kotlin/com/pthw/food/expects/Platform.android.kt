package com.pthw.food.expects

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.pthw.food.R
import com.pthw.food.ui.theme.ColorPrimary
import com.pthw.food.ui.theme.md_theme_dark_background
import com.pthw.food.ui.theme.md_theme_dark_surface
import com.pthw.food.ui.theme.md_theme_light_surface

class AndroidPlatform(override val type: PlatformType = PlatformType.Android) : Platform {

    @Composable
    override fun getScreenWidth(): Dp = LocalConfiguration.current.screenWidthDp.dp

    @Composable
    override fun getScreenHeight(): Dp = LocalConfiguration.current.screenHeightDp.dp

    @Composable
    override fun getCurrentOrientation(): Orientation {
        val configuration = LocalConfiguration.current // This needs to be called within a Composable
        return when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> Orientation.Portrait
            Configuration.ORIENTATION_LANDSCAPE -> Orientation.Landscape
            else -> Orientation.Unknown
        }
    }

    override fun initKmpNotifier() {
        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.mipmap.ic_launcher_foreground,
                showPushNotification = true,
            )
        )
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()
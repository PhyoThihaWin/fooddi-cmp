package com.pthw.food

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.mmk.kmpnotifier.notification.NotifierManager
import com.pthw.food.ui.composable.PermissionDialog
import com.pthw.food.domain.model.AppThemeMode
import com.pthw.food.expects.Logger
import com.pthw.food.ui.home.HomePage
import com.pthw.food.ui.home.HomePageViewModel
import com.pthw.food.ui.theme.FoodDiAppTheme
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun App() {
    val viewModel = koinViewModel<HomePageViewModel>()

    FoodDiAppTheme(
        darkTheme = AppThemeMode.isDarkMode(viewModel.state.value.themeCode),
    ) {
        // Home
        HomePage()

        // Permissions
        RequestNotificationPermission()
    }

    LaunchedEffect(true) {
        delay(2000)
        Logger.i("FCM token: ${NotifierManager.getPushNotifier().getToken()}")
    }
}

@Composable
fun RequestNotificationPermission() {
    val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val controller: PermissionsController = remember(factory) { factory.createPermissionsController() }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    var isGranted by remember { mutableStateOf<Boolean?>(null) }

    BindEffect(controller)

    LaunchedEffect(Unit) {
        isGranted = controller.isPermissionGranted(Permission.REMOTE_NOTIFICATION)
    }

    if (isGranted == false) {
        PermissionDialog {
            coroutineScope.launch {
                controller.providePermission(Permission.REMOTE_NOTIFICATION)
            }
        }
    }
}
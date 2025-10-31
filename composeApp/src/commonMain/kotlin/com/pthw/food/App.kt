package com.pthw.food

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.mmk.kmpnotifier.notification.NotifierManager
import com.pthw.food.domain.model.AppThemeMode
import com.pthw.food.ui.home.HomePage
import com.pthw.food.ui.home.HomePageViewModel
import com.pthw.food.ui.theme.FoodDiAppTheme
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun App() {
    val viewModel = koinViewModel<HomePageViewModel>()

    FoodDiAppTheme(
        darkTheme = AppThemeMode.isDarkMode(viewModel.appThemeMode.value),
    ) {
        // Home
        HomePage()
    }

    LaunchedEffect(true) {
        delay(2000)
        Logger.i("FCM token: ${NotifierManager.getPushNotifier().getToken()}")
    }
}
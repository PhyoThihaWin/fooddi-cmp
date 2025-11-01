package com.pthw.food.utils

import com.pthw.food.domain.model.AppThemeMode
import com.pthw.food.domain.model.FilterType
import com.pthw.food.domain.model.Localization
import fooddimultiplatform.composeapp.generated.resources.*

object ConstantValue {
    val DEFAULT_LOCALE = Localization.Myanmar.code

    const val INTERSTITIAL_COUNT = 10

    const val IMAGE_PATH =
        "https://firebasestorage.googleapis.com/v0/b/fooddi-3ca51.appspot.com/o/%s?alt=media"

    val filterList = listOf(
        FilterType(Res.drawable.ic_letter_a, Res.string.all, null),
        FilterType(Res.drawable.ic_dish, Res.string.food, "food"),
        FilterType(Res.drawable.ic_apple, Res.string.fruit, "fruit"),
        FilterType(Res.drawable.ic_vegetable, Res.string.vegetable, "vegetable"),
        FilterType(Res.drawable.ic_meat, Res.string.meat, "meat"),
        FilterType(Res.drawable.ic_sandwich, Res.string.snack, "snack"),
    )

    val settingList = listOf(
        Pair(Res.drawable.ic_settings, Res.string.chooseLanguage),
        Pair(Res.drawable.ic_round_dark_mode, Res.string.chooseThemeMode),
        Pair(Res.drawable.ic_info, Res.string.aboutApp),
        Pair(Res.drawable.ic_more_app, Res.string.moreApp),
    )

    val appThemeModes = listOf(
        AppThemeMode(Res.string.system_default_mode, AppThemeMode.SYSTEM_DEFAULT),
        AppThemeMode(Res.string.dark_mode, AppThemeMode.DARK_MODE),
        AppThemeMode(Res.string.light_mode, AppThemeMode.LIGHT_MODE)
    )
}
package com.pthw.food.utils

import com.pthw.food.domain.model.AppThemeMode
import com.pthw.food.domain.model.FilterType
import com.pthw.food.domain.model.Localization
import com.pthw.food.domain.model.SettingData
import com.pthw.food.expects.PlatformType
import com.pthw.food.expects.getPlatform
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
        SettingData(4, Res.drawable.wiki, Res.string.citation),
        SettingData(1, Res.drawable.ic_settings, Res.string.chooseLanguage),
        SettingData(2, Res.drawable.ic_round_dark_mode, Res.string.chooseThemeMode),
        SettingData(3, Res.drawable.ic_info, Res.string.aboutApp),
        SettingData(5, Res.drawable.ic_more_app, Res.string.moreApp),
    )

    val appThemeModes = listOf(
        AppThemeMode(Res.string.system_default_mode, AppThemeMode.SYSTEM_DEFAULT),
        AppThemeMode(Res.string.dark_mode, AppThemeMode.DARK_MODE),
        AppThemeMode(Res.string.light_mode, AppThemeMode.LIGHT_MODE)
    )

    val citationUrl =
        "https://my.wikipedia-on-ipfs.org/wiki/%E1%80%90%E1%80%BD%E1%80%B2%E1%80%96%E1%80%80%E1%80%BA%E1%81%8D_%E1%80%99%E1%80%85%E1%80%AC%E1%80%B8%E1%80%9E%E1%80%84%E1%80%B7%E1%80%BA%E1%80%9E%E1%80%B1%E1%80%AC_%E1%80%A1%E1%80%85%E1%80%AC%E1%80%99%E1%80%BB%E1%80%AC%E1%80%B8"
    val moreAppUrl =
        if (getPlatform().type == PlatformType.Android) "https://play.google.com/store/apps/dev?id=5729357381500909341"
        else "https://apps.apple.com/us/developer/phyo-thiha-win/id1808051048"
}
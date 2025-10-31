package com.pthw.food.domain.model

import fooddimultiplatform.composeapp.generated.resources.Res
import fooddimultiplatform.composeapp.generated.resources.locale_english
import fooddimultiplatform.composeapp.generated.resources.locale_myanmar
import org.jetbrains.compose.resources.StringResource

/**
 * Created by P.T.H.W on 21/07/2024.
 */

enum class Localization(
    val title: StringResource,
    val code: String
) {
    English(Res.string.locale_english, "en"),
    Myanmar(Res.string.locale_myanmar, "my")
}


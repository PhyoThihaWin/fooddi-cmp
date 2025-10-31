package com.pthw.food.domain.model

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * Created by P.T.H.W on 19/07/2024.
 */
data class FilterType(
    val icon: DrawableResource,
    val title: StringResource,
    val type: String?
)

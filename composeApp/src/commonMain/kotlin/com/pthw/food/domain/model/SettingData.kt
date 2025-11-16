package com.pthw.food.domain.model

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * Created by phyothihawin on 16/11/2025.
 */
data class SettingData(
    val id: Int,
    val icon: DrawableResource,
    val title: StringResource,
)

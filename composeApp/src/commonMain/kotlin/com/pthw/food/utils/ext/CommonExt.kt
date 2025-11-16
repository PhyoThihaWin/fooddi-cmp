package com.pthw.food.utils.ext

import androidx.compose.ui.platform.UriHandler

/**
 * Created by phyothihawin on 16/11/2025.
 */

fun UriHandler.go(url: String) {
    try {
        openUri(url)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
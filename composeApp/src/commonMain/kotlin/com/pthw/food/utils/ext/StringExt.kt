package com.pthw.food.utils.ext

/**
 * Created by phyothihawin on 30/10/2025.
 */

fun String.formatString(vararg args: Any?): String {
    var result = this
    args.forEach { arg ->
        result = result.replaceFirst(Regex("%[sd]"), arg.toString())
    }
    return result
}
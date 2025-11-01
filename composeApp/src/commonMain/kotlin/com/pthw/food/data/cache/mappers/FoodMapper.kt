package com.pthw.food.data.cache.mappers

import com.pthw.food.common.ext.formatString
import com.pthw.food.domain.model.Food
import com.pthw.food.utils.ConstantValue

class FoodMapper() {
    fun map(item: Food) = item.copy(
        dieEN = if (item.dieEN == "Death") "Danger" else item.dieEN,
        imgOne = item.imgOne.firebaseImage(),
        imgTwo = item.imgTwo.firebaseImage()
    )

    private fun String.firebaseImage(): String {
        return ConstantValue.IMAGE_PATH.formatString("fooddi_photo%2F$this")
    }
}
package com.pthw.food.data.mappers

import com.pthw.food.utils.ext.formatString
import com.pthw.food.domain.model.Food
import com.pthw.food.expects.PlatformType
import com.pthw.food.expects.getPlatform
import com.pthw.food.utils.ConstantValue
import org.koin.core.Koin
import org.koin.mp.KoinPlatform

class FoodMapper() {
    fun map(item: Food) = item.copy(
        dieEN = if (getPlatform().type == PlatformType.iOS && item.dieEN == "Death") "Danger" else item.dieEN,
        imgOne = item.imgOne.firebaseImage(),
        imgTwo = item.imgTwo.firebaseImage()
    )

    private fun String.firebaseImage(): String {
        return ConstantValue.IMAGE_PATH.formatString("fooddi_photo%2F$this")
    }
}
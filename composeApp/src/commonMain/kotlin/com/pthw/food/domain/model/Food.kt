package com.pthw.food.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Food")
data class Food(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "oneMM")
    val oneMM: String,

    @ColumnInfo(name = "twoMM")
    val twoMM: String,

    @ColumnInfo(name = "dieMM")
    val dieMM: String,

    @ColumnInfo(name = "oneEN")
    val oneEN: String,

    @ColumnInfo(name = "twoEN")
    val twoEN: String,

    @ColumnInfo(name = "dieEN")
    val dieEN: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "imgOne")
    val imgOne: String,

    @ColumnInfo(name = "imgTwo")
    val imgTwo: String
) {
    companion object {
        fun fake() = Food(
            id = 1,
            oneMM = "",
            twoMM = "",
            dieMM = "",
            oneEN = "Apple Sword Bean",
            twoEN = "Orange",
            dieEN = "Chest congestion| Death",
            type = "",
            imgOne = "",
            imgTwo = ""
        )

        fun fakeList() = listOf(fake(), fake(), fake(), fake(), fake(), fake(), fake())
    }

    fun getFoodOne(localeCode: String) = if (localeCode == Localization.English.code) oneEN else oneMM
    fun getFoodTwo(localeCode: String) = if (localeCode == Localization.English.code) twoEN else twoMM
    fun getFoodDie(localeCode: String) = if (localeCode == Localization.English.code) dieEN else dieMM
}
